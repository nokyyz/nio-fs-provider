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

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

import jcifs.smb.SmbFile;

/**
 * File attributes for SMB files and directories.
 */
public class SMBFileAttributes implements BasicFileAttributes {

  private final int attributes;
  public SMBFileAttributes(String uncPath, int attributes) {
    this.attributes = attributes;
  }

  @Override
  public FileTime lastModifiedTime() {
    return null;
  }

  @Override
  public FileTime lastAccessTime() {
    return null;
  }

  @Override
  public FileTime creationTime() {
    return null;
  }

  @Override
  public boolean isRegularFile() {
    return !isDirectory();
  }

  @Override
  public boolean isDirectory() {
    return (attributes & SmbFile.ATTR_DIRECTORY) != 0;
  }

  @Override
  public boolean isSymbolicLink() {
    return false;
  }

  @Override
  public boolean isOther() {
    return false;
  }

  @Override
  public long size() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public Object fileKey() {
    return null;
  }
}
