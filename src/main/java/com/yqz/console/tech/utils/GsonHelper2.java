//package com.yqz.console.utils;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.TypeAdapter;
//import com.google.gson.stream.JsonReader;
//import com.google.gson.stream.JsonWriter;
//import lombok.extern.slf4j.Slf4j;
//import org.msgpack.util.json.JSON;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.IOException;
//import java.lang.reflect.Type;
//@Slf4j
//public class GsonHelper {
//
//
//        private static Gson gson = new Gson();// 全局静态对象
//        private static Gson handleNullGson;// 全局静态对象,serializeNulls null转空串
//        private static final Logger logger = LoggerFactory
//                .getLogger(JsonHelper.class);
//
//        static {
//            handleNullGson = new GsonBuilder().serializeNulls()
//                    .registerTypeAdapter(String.class, new TypeAdapter<String>() {
//
//                        @Override
//                        public String read(JsonReader reader) throws IOException {
//                            return reader.nextString();
//                        }
//
//                        @Override
//                        public void write(JsonWriter writer, String str)
//                                throws IOException {
//                            writer.value(str == null ? "" : str);
//                        }
//                    }).registerTypeAdapter(Integer.class, new TypeAdapter<Integer>() {
//
//                        @Override
//                        public Integer read(JsonReader reader) throws IOException {
//                            return reader.nextInt();
//                        }
//
//                        @Override
//                        public void write(JsonWriter writer, Integer itg)
//                                throws IOException {
//                            writer.value(itg == null?0:itg);
//                        }
//                    }).registerTypeAdapter(Boolean.class, new TypeAdapter<Boolean>() {
//
//                        @Override
//                        public Boolean read(JsonReader reader) throws IOException {
//                            return reader.nextBoolean();
//                        }
//
//                        @Override
//                        public void write(JsonWriter writer, Boolean itg)
//                                throws IOException {
//                            writer.value(itg == null?false:itg);
//                        }
//                    }).create();
//        }
//
//        private static Gson getGsonInstant() {
//            return gson;
//        }
//
//        /**
//         * ObjectתΪJson String
//         */
//        public static <T> String ObjecttoJsonStr(T obj) {
//            String jsonStr = null;
//            try {
//                jsonStr = JSON.toJSONString(obj, SerializerFeature.DisableCircularReferenceDetect);
//            } catch (Exception e) {
//                log.error( e.getMessage());
//            }
//
//            return jsonStr;
//        }
//
//        /**
//         * StringתΪObject
//         *
//         * @param <T>
//         * @param jsonStr
//         * @return
//         */
//        @SuppressWarnings({ "unchecked", "rawtypes" })
//        public static <T> T JsonStrToObject(String jsonStr, Class cls) {
//            T obj = null;
//            try {
//                obj = (T) JSON.parseObject(jsonStr, cls);
//            } catch (Exception e) {
//                log.error( e.getMessage());
//            }
//
//            return obj;
//        }
//
//        public static <T> T JsonStrToObject(String jsonStr, TypeReference<T> type) {
//            T obj = null;
//            try {
//                obj = (T) JSON.parseObject(jsonStr, type);
//            } catch (Exception e) {
//                log.error( e.getMessage());
//            }
//
//            return obj;
//        }
//
//        /**
//         * ObjectתΪJson String
//         */
//        public static <T> String GObjecttoJsonStr(T obj) {
//            String str = null;
//            try {
//                str = getGsonInstant().toJson(obj);
//            } catch (Exception e) {
//                log.error( e.getMessage());
//            }
//            return str;
//        }
//
//        /**
//         * StringתΪObject
//         *
//         * @param <T>
//         * @param jsonStr
//         * @param cla
//         * @return
//         */
//        @SuppressWarnings({ "rawtypes", "unchecked" })
//        public static <T> T GsonStrToObject(Object jsonStr, Class cls) {
//            T obj = null;
//            try {
//                obj = (T) getGsonInstant().fromJson(jsonStr.toString(), cls);
//            } catch (Exception e) {
//                log.error( e.getMessage());
//            }
//
//            return obj;
//        }
//
//        /**
//         * StringתΪObject
//         *
//         * @param <T>
//         * @param jsonStr
//         * @param cla
//         * @return
//         */
//        @SuppressWarnings("unchecked")
//        public static <T> T GsonStrToObject(Object jsonStr, Type typeOfT) {
//            T obj = null;
//            try {
//                obj = (T) getGsonInstant().fromJson(jsonStr.toString(), typeOfT);
//            } catch (Exception e) {
//                log.error( e.getMessage());
//            }
//
//            return obj;
//        }
//
//        /**
//         * Gson反序列化时,将String null转为空串输出
//         *
//         * @param obj
//         * @return
//         */
//        public static <T> String GsonStrToObjectHandleNull(T obj) {
//            try {
//                return handleNullGson.toJson(obj);
//            } catch (Exception e) {
//                log.error( e.getMessage());
//            }
//            return null;
//        }
//
//    }
