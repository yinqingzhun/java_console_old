package com.yqz.console.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.PackageVersion;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.yqz.console.model.CarBrand;
import com.yqz.console.model.CarSerial;
import com.yqz.console.model.ReturnValue;
import com.yqz.console.model.VideoSource;

public class JsonUtils {
	static  class CheckSalacity {
	    private int id;

	    private Long vsId;

	    private Byte state;

	    private Byte label;

	    private Integer salacityOffset;

	    private String result;

	    private Byte checker;

	    private Date createTime;

	    private Date updateTime;

	    private int workDuration;

	    private String log;

	    public int getWorkDuration() {
	        return workDuration;
	    }

	    public void setWorkDuration(int workDuration) {
	        this.workDuration = workDuration;
	    }

	    public String getLog() {
	        return log;
	    }

	    public void setLog(String log) {
	        this.log = log;
	    }

	    public Integer getId() {
	        return id;
	    }

	    public void setId(Integer id) {
	        this.id = id;
	    }

	    public Long getVsId() {
	        return vsId;
	    }

	    public void setVsId(Long vsId) {
	        this.vsId = vsId;
	    }

	    public Byte getState() {
	        return state;
	    }

	    public void setState(Byte state) {
	        this.state = state;
	    }

	    public Byte getLabel() {
	        return label;
	    }

	    public void setLabel(Byte label) {
	        this.label = label;
	    }

	    public Integer getSalacityOffset() {
	        return salacityOffset;
	    }

	    public void setSalacityOffset(Integer salacityOffset) {
	        this.salacityOffset = salacityOffset;
	    }

	    public String getResult() {
	        return result;
	    }

	    public void setResult(String result) {
	        this.result = result == null ? null : result.trim();
	    }

	    public Byte getChecker() {
	        return checker;
	    }

	    public void setChecker(Byte checker) {
	        this.checker = checker;
	    }

	    public Date getCreateTime() {
	        return createTime;
	    }

	    public void setCreateTime(Date createTime) {
	        this.createTime = createTime;
	    }

	    public Date getUpdateTime() {
	        return updateTime;
	    }

	    public void setUpdateTime(Date updateTime) {
	        this.updateTime = updateTime;
	    }
	}
	private static final ObjectMapper mapper = new ObjectMapper();

	public static void main(String[] args) {

		String s = "{\"returncode\":10001, \"result\":{\"returncode\":10001,\"message\":\"重复插入\",\"result\":1}}";
		try {
			
			
			s = "{\"returncode\":10001,\"message\":\"重复插入\",\"result\":null}";
			ReturnValue<JsonNode> tv = JsonUtils.deserialize(new TypeReference<ReturnValue<JsonNode>>() {
			}, s);
			 
			System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(tv));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static {
		mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public static <T> T deserialize(Class<T> classT, String json) {
		try {
			T value = mapper.readValue(json, classT);
			return value;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T deserialize(TypeReference<T> typeR, String json) {
		try {
			T value = mapper.readValue(json, typeR);
			return value;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String serialize(Object value) {
		String s = null;
		try {
			s = mapper.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return s;
	}

	private static void crud() {
		try {

			ObjectMapper mapper = new ObjectMapper();
			InputStream is = JsonUtils.class.getClass().getResourceAsStream("resources/user.json");
			if (is != null) {
				BufferedReader in = new BufferedReader(new InputStreamReader(is));
				String line = in.readLine();
				while (line != null) {
					System.out.println(line);
				}
			}

			// String s = "[ { \"id\" : 1, \"name\" : { \"first\" : \"Yong\",
			// \"last\" : \"Mook Kim\" }, \"contact\" : [ { \"type\" :
			// \"phone/home\", \"ref\" : \"111-111-1234\"}, { \"type\" :
			// \"phone/work\", \"ref\" : \"222-222-2222\"} ]},{ \"id\" : 2,
			// \"name\" : { \"first\" : \"Yong\", \"last\" : \"Zi Lap\" },
			// \"contact\" : [ { \"type\" : \"phone/home\", \"ref\" :
			// \"333-333-1234\"}, { \"type\" : \"phone/work\", \"ref\" :
			// \"444-444-4444\"} ]}]";
			String s = "{}";
			JsonNode root = mapper.readTree(s);

			String resultOriginal = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
			System.out.println("Before Update " + resultOriginal);

			// 1. Update id to 1000
			((ObjectNode) root).put("id", 1000L);

			// 2. If middle name is empty , update to M
			JsonNode nameNode = root.path("name");
			if (!nameNode.isMissingNode()) {

				if ("".equals(nameNode.path("middle").asText())) {
					((ObjectNode) nameNode).put("middle", "M");
				}

				// 3. Create a new field in nameNode
				((ObjectNode) nameNode).put("nickname", "mkyong");

				// 4. Remove last field in nameNode
				((ObjectNode) nameNode).remove("last");
			}
			// 5. Create a new ObjectNode and add to root
			ObjectNode positionNode = mapper.createObjectNode();
			positionNode.put("name", "Developer");
			positionNode.put("years", 10);
			((ObjectNode) root).set("position", positionNode);

			// 6. Create a new ArrayNode and add to root
			ArrayNode gamesNode = mapper.createArrayNode();

			ObjectNode game1 = mapper.createObjectNode();
			game1.put("name", "Fall Out 4");
			game1.put("price", 49.9);

			ObjectNode game2 = mapper.createObjectNode();
			game2.put("name", "Dark Soul 3");
			game2.put("price", 59.9);

			gamesNode.add(game1);
			gamesNode.add(game2);
			((ObjectNode) root).set("games", gamesNode);

			// 7. Append a new Node to ArrayNode
			ObjectNode email = mapper.createObjectNode();
			email.put("type", "email");
			email.put("ref", "abc@mkyong.com");

			// JsonNode contactNode = root.path("contact");
			// ((ArrayNode) contactNode).add(email);

			String resultUpdate = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
			System.out.println("After Update " + resultUpdate);

			ObjectNode treeRootNode = mapper.createObjectNode();
			((ObjectNode) treeRootNode).put("car", "Alfa Romio");
			((ObjectNode) treeRootNode).put("price", "54000");
			((ObjectNode) treeRootNode).put("model", "2013");
			ArrayNode arrayNode = ((ObjectNode) treeRootNode).putArray("colors");
			arrayNode.add("GRAY");
			arrayNode.add("BLACK");
			arrayNode.add("WHITE");
			resultUpdate = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(treeRootNode);
			System.out.println("After Update " + resultUpdate);

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static ObjectMapper getObjectMapper() {
		ObjectMapper objMapper = new ObjectMapper();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		JavaTimeModule javaTimeModule = new JavaTimeModule();
		javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
		javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));
		javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormatter));
		javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormatter));
		// javaTimeModule.addDeserializer(Date.class, new
		// SimpleDateDeserializer("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"));
		objMapper.registerModule(javaTimeModule);
		objMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		objMapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
		objMapper.enable(MapperFeature.DEFAULT_VIEW_INCLUSION);
		objMapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
		objMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objMapper.disable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);
		objMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		objMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
		objMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		objMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		objMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		objMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

		return objMapper;
	}

	private static void deserialize() {
		String s = "{    \"returnCode\": 0,    \"message\": null,    \"result\": [        {            \"seriesId\": 0,            \"confidence\": null,            \"seriesName\": null,            \"brandName\": \"aaaa\",            \"price\": null,            \"picture\": \"aaaaaaaaaaaaaaaaaaaa\"        },        {            \"seriesId\": 0,           \"confidence\": null,            \"seriesName\": null,            \"brandName\": \"bbbb\",            \"price\": null,            \"picture\": \"bbbbbbbbbbbbbbbbbbbbb\"        }    ]}";
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule newModule = new SimpleModule("SimpleDateDeserializer", PackageVersion.VERSION);
		// newModule.addDeserializer(Date.class, new
		// StdScalarDeserializer<Date>(Date.class) {
		// @Override
		// public Date deserialize(JsonParser p, DeserializationContext ctxt)
		// throws IOException, JsonProcessingException {
		// // TODO Auto-generated method stub
		// return null;
		// }
		//
		// });
		mapper.registerModule(newModule);
		ReturnValue<List<CarSerial>> o = null;
		try {

			// mapper.getTypeFactory().constructGeneralizedType(baseType,
			// superClass)
			o = mapper.readValue(s, new TypeReference<ReturnValue<List<CarSerial>>>() {
			});

			// s = "{ \"returnCode\": 100, \"message\": \"hello\"}";
			// o = mapper.readValue(s, new
			// TypeReference<ReturnValue<CarBrand>>() {
			// });

			// System.out.println(new
			// ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(o));

			CarSerial c = new CarSerial();
			// mapper.readerForUpdating(c).readValue(mapper.writeValueAsString(o.getResult().get(0)));

			// System.out.println(new
			// ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(c));

			s = "{  \"createTime\": \"2015-8-8\",  \"brandName\": \"das auto\",       \"list\": [        {            \"seriesId\": 0,            \"confidence\": null,            \"seriesName\": null,            \"brandName\": \"aaaa\",            \"price\": null,            \"picture\": \"aaaaaaaaaaaaaaaaaaaa\"        },        {            \"seriesId\": 0,           \"confidence\": null,            \"seriesName\": null,            \"brandName\": \"bbbb\",            \"price\": null,            \"picture\": \"bbbbbbbbbbbbbbbbbbbbb\"        }    ]}";
			CarBrand carBrand = mapper.readValue(s, CarBrand.class);
			// System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(carBrand));

			s = "{\"id\":1,\"siteType\":3,\"vid\":\"w03816d2raf\",\"title\":\"车评人试车时遭遇惨烈车祸\",\"category\":\"汽车\",\"thumb\":\"sdfsdgfdghfghtghghfgdsf\",\"tags\":\",斯柯达,明锐,紧凑车型,事故车,二手车,评测\",\"logo\":\"\",\"navBar\":\"\",\"state\":2,\"athmMp4Url\":\"v-PtT-DzpyCcqv6xNU25neTMkcc=/FjgJQXuH7OresQL4zgRqYG5bZ64x\",\"athmM3u8Url\":\"retretertyuiopytretretret\",\"webUrl\":\"http://v.qq.com/x/page/w03816d2raf.html\",\"publisherId\":\"\",\"publisherName\":\"\",\"publisherDate\":1488729600000,\"countPv\":115000,\"countReply\":0,\"countLike\":0,\"vSize\":88942245,\"vWidth\":1280,\"vHeight\":720,\"segsCount\":2,\"vMilliseconds\":679000,\"aMilliseconds\":679000,\"createTime\":\"2015-12-18 12:32:32\",\"updateTime\":\"2015-12-18 12:32:32\",\"timestamp\":1497591268000,\"clientId\":\"client\",\"collectorId\":100,\"collectorName\":\"jack\"}";
			mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

			VideoSource vs = mapper.readValue(s, VideoSource.class);
			mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
			System.out.println(getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(vs));

			s = "{    \"task_id\": 4,    \"work_duration\": 61656,    \"retry_count\": 0,    \"log\": \"dsfadfsfdfaf\",    \"store_list\": [        {            \"vs_id\": 3,            \"seg_seq\": 1,            \"seg_size\": 45643634,            \"seg_local_path\": \"/var/jackson/ewijksd/dsf.mp4\", \"v_milliseconds\": 134434,            \"a_milliseconds\": 134434        }     ]}";
			// VideoTaskReportVO report = mapper.readValue(s,
			// VideoTaskReportVO.class);
			// System.out.println(new
			// ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(report));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
