package framework.generic.mybatis.paginator.jackson2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import framework.generic.mybatis.paginator.domain.PageList;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class PageListJsonSerializer extends JsonSerializer<PageList> {
	@Override
	public void serialize(PageList value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", value.getPaginator().getTotalCount());
		map.put("totalPages", value.getPaginator().getTotalPages());
		map.put("page", value.getPaginator().getPage());
		map.put("limit", value.getPaginator().getLimit());
		map.put("items", new ArrayList(value));
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(jgen, map);
	}
}
