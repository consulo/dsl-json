package com.dslplatform.json.nullaway;

import com.dslplatform.json.CompiledJson;
import com.dslplatform.json.DslJson;
import org.jspecify.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class NestedCollectionRoundtripTest {

	@CompiledJson
	public static class Entry {
		public String id = "";
		public @Nullable List<String> tags = new ArrayList<>();
	}

	@CompiledJson
	public static class Holder {
		public @Nullable List<Entry> entries = new ArrayList<>();
		public @Nullable Map<String, String> active = new LinkedHashMap<>();
	}

	private final DslJson<Object> dslJson = new DslJson<>();

	@Test
	public void roundtrip() throws IOException {
		Entry entry = new Entry();
		entry.id = "a";
		entry.tags = new ArrayList<>(List.of("x", "y"));
		Holder holder = new Holder();
		holder.entries = new ArrayList<>(List.of(entry));
		holder.active = new LinkedHashMap<>(Map.of("type", "a"));

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		dslJson.serialize(holder, os);

		Holder read = dslJson.deserialize(Holder.class, new ByteArrayInputStream(os.toByteArray()));
		Assert.assertNotNull(read);
		List<Entry> entries = read.entries;
		Assert.assertNotNull(entries);
		Assert.assertEquals(1, entries.size());
		Assert.assertEquals("a", entries.get(0).id);
		Assert.assertEquals(List.of("x", "y"), entries.get(0).tags);
		Assert.assertEquals(Map.of("type", "a"), read.active);
	}
}
