package com.yqz.console.tech.model;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.yqz.console.tech.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

public class Person {
	private final static Logger logger = LoggerFactory.getLogger(Person.class);

	public static enum Sex {
		MALE, FEMALE
	}

	private int id;
	private String name;
	private Sex gender;
	private int age;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Sex getGender() {
		return gender;
	}

	public void setGender(Sex gender) {
		this.gender = gender;
	}

	public String getName() {
		System.out.println(name);
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static List<Person> createRoster() {
		return new ArrayList<Person>();
	}

	public void printPerson() {
		System.out.println();
	}

	public String toString() {
		return String.format("name:%s,age=%s,sex=%s", this.name, this.age, this.gender);
	}

	static class Car {
		public static Car create(final Supplier<Car> supplier) {
			return supplier.get();
		}

		public static void collide(final Car car) {
			System.out.println("Collided " + car.toString());
		}

		public void follow(final Car another) {
			System.out.println("Following the " + another.toString());
		}

		public void repair() {
			System.out.println("Repaired " + this.toString());
		}
	}

	public static void main(String[] args) throws ParseException {
		final Car car = Car.create(Car::new);
		final List<Car> cars = Arrays.asList(car);
		cars.forEach(Car::collide);
		cars.forEach(Car::repair);
		cars.forEach(car::follow);

		// logger.info("hello");

		int d = Stream.of(Arrays.asList(1), Arrays.asList(2, 3), Arrays.asList(4, 5, 6))
				.flatMap(childList -> childList.stream()).sorted(Integer::compare).mapToInt(item -> item)
				.reduce((a, b) -> a + b).getAsInt();
		System.out.println(d);
		ArrayList<Person> persons = new ArrayList<>();
		Integer[] transactionsIds = persons.parallelStream().filter(t -> t.getGender() == Sex.FEMALE)
				.sorted(Comparator.comparing(Person::getAge).reversed()).map(Person::getId).toArray(Integer[]::new);

		Pattern pattern = Pattern.compile("\\d+℃");
		Matcher matcher = pattern.matcher("周五 03月24日 (实时：2℃)");
		if (matcher.find()) {
			System.out.println(matcher.group());
		}

		Person p = new Person() {
			{
				setAge(10);
				setGender(Sex.FEMALE);
				setName("jack");
			}
		};
		String g = "{\"UserId\":21913305,\"Sex\":1,\"Nickname\":\"请叫我头头哥\",\"HeadImage\":\"http://i3.autoimg.cn/usercenter//g21/M0C/9B/36/120X120_0_q87_autohomecar__wKgFWld13-CAKJlTAAC4Ur_u9e4709.jpg\"}";

		Map m = ObjectUtils.objectToMap(p);
		p = ObjectUtils.mapToObject(m, Person.class);
		ObjectMapper objMapper = new ObjectMapper();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		JavaTimeModule javaTimeModule = new JavaTimeModule();
		javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
		javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));
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
		// objMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		try {
			String ps = objMapper.writeValueAsString(ReturnValue.buildSuccessResult(p));
			ReturnValue rv = objMapper.readValue(ps, new TypeReference<ReturnValue<Person>>() {
			});

			FollowedUserVO v = objMapper.readValue(g, FollowedUserVO.class);

			String s = "{\"rowcount\":2,\"pagecount\":2,\"pageindex\":1,\"list\": [{\"userId\":1354239,\"realName\":\"\",\"sex\":1,\"cNickname\":\"nick1354239\",\"qNickname\":\"nick1354239\",\"summary\":\"Just do it\",\"isFollow\":0,\"headImage\":\"http://i1.autoimg.cn/album/userheaders/2012/11/28/db2bfb9f-4ad5-4780-84d1-4882a30ca121_120X120.jpg\"},{\"userId\":1354222,\"realName\":\"\",\"sex\":1,\"cNickname\":\"nick1354222\",\"qNickname\":\"nick1354222\",\"summary\":\"Just do do\",\"isFollow\":1,\"headImage\":\"http://i1.autoimg.cn/album/userheaders/2012/11/28/db2bfb9f-4ad5-4780-84d1-4882a30ca121_120X120.jpg\"}]}";

			RelationVO r = new ObjectMapper().readValue(s, RelationVO.class);
			System.out.println(r);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(p);

	}

}

class BasePageModel {
	private int pageindex;
	private int pagecount;
	private int rowcount;

	public int getPageindex() {
		return pageindex;
	}

	public void setPageindex(int pageindex) {
		this.pageindex = pageindex;
	}

	public int getPagecount() {
		return pagecount;
	}

	public void setPagecount(int pagecount) {
		this.pagecount = pagecount;
	}

	public int getRowcount() {
		return rowcount;
	}

	public void setRowcount(int rowcount) {
		this.rowcount = rowcount;
	}
}

class RelationVO extends BasePageModel {
	private List<Relation> list;

	public List<Relation> getList() {
		return list;
	}

	public void setList(List<Relation> list) {
		this.list = list;
	}
}

class Relation {
	private Integer userId;
	private String realName;
	private Integer sex;
	private String cNickname;
	private String qNickname;
	private String summary;
	private Integer isFollow;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getcNickname() {
		return cNickname;
	}

	public void setcNickname(String cNickname) {
		this.cNickname = cNickname;
	}

	public String getqNickname() {
		return qNickname;
	}

	public void setqNickname(String qNickname) {
		this.qNickname = qNickname;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Integer getIsFollow() {
		return isFollow;
	}

	public void setIsFollow(Integer isFollow) {
		this.isFollow = isFollow;
	}

	public String getHeadImage() {
		return headImage;
	}

	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}

	private String headImage;
}

class FollowedUserVO {
	@JsonProperty("UserId")
	private int userId;
	@JsonProperty("RealName")
	private String realName;
	@JsonProperty("Sex")
	private int sex;
	@JsonProperty("Nickname")
	private String nickname;
	@JsonProperty("HeadImage")
	private String headImage;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHeadImage() {
		return headImage;
	}

	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}
}
