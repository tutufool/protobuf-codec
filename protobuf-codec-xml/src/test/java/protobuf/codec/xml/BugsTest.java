package protobuf.codec.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Assert;
import org.junit.Test;

import protobuf.codec.Codec;
import protobuf.codec.Codec.Feature;
import protobuf.codec.xml.TypesProtoBuf.Version;

public class BugsTest {

	@Test
	public void testLongText() throws Exception {
		try {

			Version before = Version.newBuilder().setName("baaaaaaaaaaaaaasssssssssssssssssssaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab")//
					.build();

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			Codec codec = new XmlCodec();

			codec.fromMessage(before, outputStream);
			System.out.println(outputStream.toString());

			Version after = codec.toMessage(Version.class, new ByteArrayInputStream(outputStream.toByteArray()));

			Assert.assertEquals(after, before);
			Assert.assertEquals(after.getName(), before.getName());
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testNullChar() {

		try {

			Version target = Version.newBuilder().setName("abc def").build();

			Codec codec = new XmlCodec();
			codec.setFeature(Feature.REPLACE_EMPTY_CHAR, Boolean.TRUE);

			Writer writer = new StringWriter();

			Version before = Version.newBuilder().setName("abc\u0007def").build();
			codec.fromMessage(before, writer);
			System.out.println(writer.toString());
			Version after = codec.toMessage(Version.class, new StringReader(writer.toString()));
			Assert.assertEquals(target, after);

			writer = new StringWriter();

			Version before1 = Version.newBuilder().setName("abc\u0000def").build();
			codec.fromMessage(before1, writer);
			System.out.println(writer.toString());
			after = codec.toMessage(Version.class, new StringReader(writer.toString()));
			Assert.assertEquals(target, after);

		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}

	}

}
