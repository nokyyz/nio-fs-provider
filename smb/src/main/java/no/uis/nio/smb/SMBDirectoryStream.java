/*
 Copyright 2012-2013 University of Stavanger, Norway

 Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package no.uis.nio.smb;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jcifs.smb.SmbFile;

/**
 * Iterates over SMB entries in an SMB directory. 
 */
public class SMBDirectoryStream implements DirectoryStream<Path> {

  private final SMBPath smbFile;
  private final java.nio.file.DirectoryStream.Filter<? super Path> filter;
  private final SMBFileSystemProvider provider;
  private boolean closed;
  
  public SMBDirectoryStream(SMBFileSystemProvider provider, SMBPath smbFile, java.nio.file.DirectoryStream.Filter<? super Path> filter) {
    this.smbFile = smbFile;
    this.filter = filter;
    this.provider = provider;
  }

  @Override
  public void close() throws IOException {
    closed = true;
  }

  @Override
  public Iterator<Path> iterator() {
    if (closed) {
      throw new IllegalStateException("Already closed");
    }
    try {
      SmbFile[] files = smbFile.getSmbFile().listFiles();
      List<Path> paths = new ArrayList<Path>(files.length);
      for (SmbFile file : files) {
        SMBPath p = new SMBPath(provider, file.getURL().toURI());
        if (filter == null || filter.accept(p)) {
          paths.add(p);
        }
      }
      return paths.iterator();
    } catch(IOException | URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }
}
