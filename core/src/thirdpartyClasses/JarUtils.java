package thirdpartyClasses;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

/**
 * This class helps to get a list of FileHandle inside the project assets folder (when the project is running from within a Jar).
 * See {@link #listFromJarIfNecessary(String)} or {@link #listFromJarIfNecessary(JarFileFilter)}
 */
public class JarUtils {
 
    private static final String JAR_EXTENSION = ".jar";
 
    public static FileHandle[] listFromJarIfNecessary(String folderPath) {
        return listFromJarIfNecessary(new JarFileFilter(folderPath));
    }
 
    public static FileHandle[] listFromJarIfNecessary(JarFileFilter jarFileFilter) {
        if (shouldLoadFromJar()) {
            Array<FileHandle> files = getFileHandleArrayList(jarFileFilter);
            return getFileHandlesArray(files);
        } else {
            // Simply load it the libGDX way.
            return Gdx.files.internal(jarFileFilter.getFolderPath()).list(jarFileFilter);
        }
    }
 
    /**
     * @return True if the project runs of desktop and if it is run from a jar file false otherwise.
     */
    private static boolean shouldLoadFromJar() {
        return getJarFile() != null;
    }
 
    /**
     * @return The project jar file if any, null otherwise.
     */
    private static File getJarFile() {
        try {
            File jarFile = new File(JarUtils.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            if (jarFile.getAbsolutePath().endsWith(JAR_EXTENSION)) {
                return jarFile; // Only return the file if it is a jar file.
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
 
    /**
     * @param jarFileFilter The filter for the jar files.
     * @return An Array object filled with the FileHandle matching the given filter.
     */
    private static Array<FileHandle> getFileHandleArrayList(JarFileFilter jarFileFilter) {
        File jarFile = getJarFile();
        if (jarFile == null) return new Array<>(); // We check even if this should never happen.
 
        Array<FileHandle> files = new Array<>();
        try (ZipFile zf = new ZipFile(jarFile.getAbsoluteFile())) {
            Enumeration<? extends ZipEntry> e = zf.entries();
            String filePrefixPath = jarFileFilter.getFolderPath();
            while (e.hasMoreElements()) { // Go through all the entries.
                ZipEntry zipEntry = e.nextElement();
                if (zipEntry.getName().length() <= jarFileFilter.getFolderPath().length())
                    continue;// Skip path that are not long enough to match the folder name.
 
                String simpleName = zipEntry.getName().substring(filePrefixPath.length()); // Get the file name without the folder prefix.
                if (zipEntry.getName().startsWith(filePrefixPath) && jarFileFilter.acceptZipEntry(simpleName)) {
                    // If the folder prefix match and the filter accept the file short name, we add it to the list.
                    FileHandle internal = Gdx.files.internal(zipEntry.getName());
                    files.add(internal);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }
 
    /**
     * Convert an Array object of FileHandle to an array structure of FileHandle.
     *
     * @param files An Array object filled with FileHandle.
     * @return A array structure of FileHandle.
     */
    private static FileHandle[] getFileHandlesArray(Array<FileHandle> files) {
        FileHandle fileHandle[] = new FileHandle[files.size];
        for (int i = 0; i < files.size; i++) {
            fileHandle[i] = files.get(i);
        }
        return fileHandle;
    }
 
    /**
     * This class define a new filter method that will be used for filtering zip entries (file within your final jar).
     */
    public static class JarFileFilter implements FileFilter {
 
        private static final String FOLDER_SEPARATOR = "/"; // To avoid getting map subfolders files.
 
        /**
         * Because the jar entries displays the full path of the file (from within the jar), this folderPath helps you
         * to get the file short name. Ex: to list entries from the folder "folder1/folder2/" you should set this folderPath to
         * "folder1/folder2/" this will help you to strip this part from your file name in the acceptZipEntry method.
         */
        private final String folderPath;
        /**
         * Whether to accept the file located in a subfolder of the given folderPath.
         */
        private boolean acceptSubFoldersFiles;
 
        public JarFileFilter(String folderPath, boolean acceptSubFoldersFiles) {
            this.folderPath = folderPath;
            this.acceptSubFoldersFiles = acceptSubFoldersFiles;
        }
 
        public JarFileFilter(String folderPath) {
            this(folderPath, false);
        }
 
        /**
         * This method will be called on zip entries when the project is run from a jar file.
         *
         * @param zipEntryShortName The short name for this file.
         * @return True if the file with this short name should be accepted.
         */
        public boolean acceptZipEntry(String zipEntryShortName) {
            if (acceptSubFoldersFiles) {
                return true;
            }
            // If we only accept file from the same directory then we should not encounter any folder separator character.
            return !zipEntryShortName.contains(FOLDER_SEPARATOR);
 
        }
 
        public String getFolderPath() {
            return folderPath;
        }
 
        @Override
        public boolean accept(File pathname) {
            // This method will be called when the project is running outside a jar file so we accept all the file by default here.
            return true;
        }
    }
 
}