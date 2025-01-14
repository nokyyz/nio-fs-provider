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

package no.uis.nio.webdav;
//CHECKSTYLE:OFF

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import no.uis.nio.commons.AbstractTest;
import no.uis.nio.commons.CatalogCreatorMock;

import org.junit.Assume;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class WebdavPathTest extends AbstractTest {

  @Rule
  public ExpectedException exception = ExpectedException.none();

  @Test
  public void nomalizeTest() throws Exception {
    Assume.assumeNotNull(testprops);
    Path path = createRelativeTestPath("nohost", -1, "/webdav/../test/something");
    Path result = path.normalize();

    assertThat(result.isAbsolute(), is(true));
  }

  @Test
  public void newFileSystemWebdav() throws Exception {
    Assume.assumeNotNull(testprops);
    URI uri = createTestUri("webdav", "lportal-test.uis.no", -1, null);

    FileSystem fs = FileSystems.newFileSystem(uri, null);

    assertThat(fs, is(notNullValue()));
  }

  @Test
  public void newFileSystemWebdavs() throws Exception {
    Assume.assumeNotNull(testprops);
    URI uri = createTestUri("webdavs", "lportal-test.uis.no", -1, null);

    FileSystem fs = FileSystems.newFileSystem(uri, null);

    assertThat(fs, is(notNullValue()));
  }

  @Test
  public void getURI() throws Exception {
    Assume.assumeNotNull(testprops);
    URI uri = createTestUri("webdav", "lportal-test.uis.no", -1, null);

    Path path = Paths.get(uri);

    assertThat(path, is(notNullValue()));
  }

  @Test
  public void getNewURI() throws Exception {
    Assume.assumeNotNull(testprops);
    URI uri = createTestUri("webdav", "lportal-test.uis.no", -1, null);

    Path path = Paths.get(uri);

    assertThat(path, is(notNullValue()));
  }

  @Test
  public void getCreateChildPath() throws Exception {
    Assume.assumeNotNull(testprops);
    URI uri = createTestUri("webdav", "lportal-test.uis.no", -1, "/webdav/test2");
    Path path = Paths.get(uri);
    Path newPath = Files.createDirectories(path);
    assertThat(newPath, is(notNullValue()));
  }

  @Test
  public void copyFiles() throws Exception {
    Assume.assumeTrue(false); // skip this test
    Assume.assumeNotNull(testprops);
    File src = File.createTempFile("webdavtest", ".txt");
    FileWriter fw = new FileWriter(src);
    fw.append("test test");
    fw.close();

    URI uriTo = createTestUri("webdav", "lportal-test.uis.no", -1, "/webdav/test2/file.txt");
    Path pathTo = Paths.get(uriTo);
    Files.copy(src.toPath(), pathTo, StandardCopyOption.REPLACE_EXISTING);
  }

  @Test
  public void deleteNonexistingFile() throws Exception {
    Assume.assumeTrue(false); // skip this test
    Assume.assumeNotNull(testprops);
    URI uri = createTestUri("webdav", "lportal-test.uis.no", -1,
      String.format("/webdav/test2/file-%s.txt", UUID.randomUUID().toString()));
    Path path = Paths.get(uri);
    exception.expect(NoSuchFileException.class);
    Files.delete(path);
  }

  @Test
  public void deleteFile() throws Exception {
    Assume.assumeTrue(false); // skip this test
    Assume.assumeNotNull(testprops);
    URI uri = createTestUri("webdav", "lportal-test.uis.no", -1, "/webdav/test2/file.txt");
    Path path = Paths.get(uri);
    Assume.assumeTrue(Files.exists(path));
    Files.delete(path);
  }

  @Test
  public void deleteWrongHost() throws Exception {
    Assume.assumeNotNull(testprops);
    URI uri = createTestUri("webdav", "non-existing-host", -1, "/");
    exception.expect(is(IOException.class));
    Path path = Paths.get(uri);
    Files.delete(path);
  }

  @Test
  public void testCatalogCreator() throws Exception {
    Assume.assumeTrue(false); // skip this test
    Assume.assumeNotNull(testprops);
    CatalogCreatorMock cc = new CatalogCreatorMock();
    URI uri = createTestUri("webdav", "lportal-test.uis.no", -1, "/webdav/test2/2012/emne/B/");
    Path outPath = Paths.get(uri);
    cc.createCatalog(outPath);
  }

  private WebdavPath createRelativeTestPath(String host, int port, String path) throws URISyntaxException {
    String username = testprops.getProperty("webdav.user");
    String password = testprops.getProperty("webdav.password");

    return new WebdavPath(new WebdavFileSystem(new WebdavFileSystemProvider(), new URI(null, username + ':' + password, host,
      port, null, null, null)), path);
  }
}
