namespace java com.samutamm.fileserver

struct FileObject {
  1: string name;
  2: binary file;
}

service FileServer {
  list<FileObject> listFiles();
  bool addFile(1: string path, 2: string name);
  bool deleteFile(1: string path);
}

