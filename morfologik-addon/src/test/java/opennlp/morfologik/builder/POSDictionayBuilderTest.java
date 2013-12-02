/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package opennlp.morfologik.builder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Properties;

import junit.framework.TestCase;
import opennlp.morfologik.lemmatizer.MorfologikLemmatizer;

import org.junit.Test;

public class POSDictionayBuilderTest extends TestCase {

  @Test
  public void testBuildDictionary() throws Exception {
    MorfologikDictionayBuilder builder = new MorfologikDictionayBuilder();
    File dictInFile = new File(POSDictionayBuilderTest.class.getResource(
        "/dictionaryWithLemma.txt").getFile());

    File dictOutFile = File.createTempFile(
        POSDictionayBuilderTest.class.getName(), ".dict");

    builder.build(dictInFile, dictOutFile, Charset.forName("UTF-8"), "+", true,
        true);

    MorfologikLemmatizer ml = new MorfologikLemmatizer(dictOutFile.toURI()
        .toURL());

    assertNotNull(ml);
  }

  @Test
  public void testPropertiesCreation() throws Exception {

    Charset c = Charset.forName("iso-8859-1");
    String sep = "_";
    boolean pref = true;
    boolean inf = true;
    Properties p = createPropertiesHelper(c, sep, pref, inf);

    assertEquals(c.name(), p.getProperty("fsa.dict.encoding"));
    assertEquals(sep, p.getProperty("fsa.dict.separator"));
    assertEquals(pref,
        Boolean.parseBoolean(p.getProperty("fsa.dict.uses-prefixes")));
    assertEquals(inf,
        Boolean.parseBoolean(p.getProperty("fsa.dict.uses-infixes")));

    pref = false;
    inf = true;
    p = createPropertiesHelper(c, sep, pref, inf);
    assertEquals(pref,
        Boolean.parseBoolean(p.getProperty("fsa.dict.uses-prefixes")));
    assertEquals(inf,
        Boolean.parseBoolean(p.getProperty("fsa.dict.uses-infixes")));

    pref = true;
    inf = false;
    p = createPropertiesHelper(c, sep, pref, inf);
    assertEquals(pref,
        Boolean.parseBoolean(p.getProperty("fsa.dict.uses-prefixes")));
    assertEquals(inf,
        Boolean.parseBoolean(p.getProperty("fsa.dict.uses-infixes")));
  }

  private Properties createPropertiesHelper(Charset c, String sep,
      boolean pref, boolean inf) throws IOException {
    MorfologikDictionayBuilder builder = new MorfologikDictionayBuilder();
    File f = File.createTempFile(POSDictionayBuilderTest.class.getName(),
        ".info");
    builder.createProperties(c, sep, pref, inf, f);

    InputStream is = new FileInputStream(f);

    Properties prop = new Properties();
    prop.load(is);
    is.close();
    f.delete();
    return prop;
  }

}
