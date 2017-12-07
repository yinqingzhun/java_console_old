package com.yqz.console;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.yqz.console.json.JsonUtils;
import com.yqz.console.model.BrandItem;
import com.yqz.console.model.ReturnValue;
import com.yqz.console.model.SerialItem;
import com.yqz.console.model.SerialItems;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CarSerialDemo {
	public static void main(String[] args) {

		List<Integer> hotIds = Arrays.asList(875,22,354,359,99,50,578,530,3420,87,2562,207,15,38,3175,439,101,2073,772,2304,389,293,396,729,2293,989,655,2348,413,841,49,513,94,2120,467,565,397,688,395,705,2085,411,57,2091,889,409,2118,608,2045,2180,77,437,2001,577,2523,2051,2742,47,756,790,558,505,554,2617,2857,149,63,351,492,660,433,168,816,694,85,533,379,2230,914,844,476,444,3086,509,377,459,89,826,3066,693,2944,2178,432,797,876,732,825,799,2131,431,525,363,449,267,488,696,3467,2034,19,801,68,493,482,2647,3497,2026,130,2302,824,138,383,866,2200,928,597,463,697,454,388,498,540,613,458,2155,545,2141,230,106,252,295,23,306,174,539,103,291,2093,595,596,126,823,144,205,518,962,232,76,927,879,3994,762,122,345,814,895,572,2261,3425,2122,535,727,460,67,532,503,854,531,142,2046,609,2049,746,290,635,360,2252,355,804,566,132,3757,475,2725,523,996,25,2833,690,2472,2331,490,170,2964,575,920,451,487,468,394,428,494,2336,2109,510,860,670,2241,904,3002,489,611,4349,501,491,3385,300,479,443,2125,2209,104,24,909,235,182,791,2649,2606,663,2722,506,416,83,1006,668,760,344,561,673,913,424,855,857,90,2610,2349,2192,731,2611,569,393,640,2530,3177,414,852,376,434,2094,685,940,420,2445,2636,325,91,581,594,583,2156,284,815,2680,651,970,2563,484,605,3635,464,3844,1008,843,2531,6,2648,2244,361,562,426,683,440,2943,231,939,429,380,2095,211,3427,258,465,233,571,582,574,556,188,332,4330,316,2157,527,84,502,830,2400,2344,3355,3082,2378,406,2242,191,277,2604,2138,4333,2840,2318,478,930,2211,661,590,658,2709,128,536,133,827,275,2215,552,2177,185,227,3680,2312,186,2674,3024,421,2245,641,298,3423,304,520,2455,2653,367,818,204,2111,417,184,3174,4038,3424,567,612,2234,2352,2213,2108,2402,2846,392,617,2031,2082,847,3537,570,2598,2796,2982,2784,862,2132,4377,3182,2171,2845,3439,3438,3116,238,3192,3724,2655,3572,840,907,2868,2161,2440,328,2438,781,3131,179,662,981,2504,3215,13,997,3034,2202,2110,624,2158,3469,461,2710,2453,3202,742,3456,2481,2306,2957,956,3421,401,2811,3444,2613,399,730,849,2233,2254,2150,194,3124,2901,2850,2489,257,2327,864,3374,457,2464,853,976,2212,739,3180,3025,3436,978,568,3067,2477,2521,2112,2081,2003,2539,959,2596,308,2106,691,3203,3169,3087,3408,2143,3071,2072,222,2875,3042,842,2340,2102,427,2166,2151,2724,400,330,870,2582,2321,3083,2593,3696,3076,3160,2069,3340,481,867,2149,4014,3090,2339,2959,2225,761,3830,131,517,3094,2305,720,2267,627,654,2067,1013,2492,977,1012,820,991,3308,2454,3054,2520,2036,2042,845,3909,2104,2597,2750,2189,2816,2536,2221,2900,3910,2114,2594,2403,2163,957,2897,839,973,1015,3555,3400,2870,778,3692,2814,2848,2416,2145,2538,4071,856,2052,902,507,2507,969,2187,2038,2076,2159,2167,619,737,2650,4081,787,3883,3280,3173,979,2240,783,883,3445,2210,3443,402,2320,2223,988,974,2307,950,941,2273,933,848,734,3569,865,735,621,3753,667,2522,935,3135,2983,2328,2170,2804,2498,3127,2591,993,3072,2258,2239,2195,2039,2812,2595,2308,2874,2876,2204,2620,2823,2825,637,2586,2934,3532,3500,943,698,924,3329,2436,2173,885,2815,2822,2064,716,931,2342,2370,2818,3832,2022,2880,2621,2373,2790,832,2071,2699,2007,2346,2798,2301,2376,2183,2634,3458,2817,2939,2257,3937,2549,2879,2372,949,2797,2592,2030,2219,2127,3609,2631,2437,2795,2933,2630,2216,2289,2079,701,3499,1020,954,916,910,2103,998,2169,2040,2096,741,2384,937,3501,915,967,2878,1019,2877,3608,2343,2048,618,2509,3029,953,745,3826,2199,2633,2164,2260,2028,628,2002,2375,2819,2033,922,968,2907,2222,2101,1011,807,944,2632,2235,2351,2077,2099,3039,817,2220,2637,2635,2350,2374,2385);

		CarSerialDemo car = new CarSerialDemo();
		List<BrandItem> list = car.getCarBrandList();
		final AtomicLong i = new AtomicLong(0);
		list.stream().forEach(brand -> {
			System.out.println(brand.getName());
			List<SerialItem> ss = car.getCarSerialList(brand.getId());
			ss.forEach(serial -> {
				if (hotIds.contains(serial.getId()))
					System.out.println(String.format("%s%s", 
							//i.incrementAndGet(),
							"",//serial.getName().startsWith(brand.getName()) ? "" : brand.getName(),
									serial.getName()));
			});
		});

	}

	private List<BrandItem> getCarBrandList() {
		String url = "http://car.api.autohome.com.cn/v1/javascript/brand.ashx?_appid=car&state=0x000f&typeid=0";

		OkHttpClient okHttpClient = new OkHttpClient();
		Request request = new Request.Builder().url(url).build();
		Call call = okHttpClient.newCall(request);
		try {
			Response response = call.execute();
			String body = response.body().string();
			ReturnValue<BrandItems> rv = JsonUtils.deserialize(new TypeReference<ReturnValue<BrandItems>>() {
			}, body);

			if (rv != null) {
				BrandItems bs = rv.getResult();
				return Arrays.asList(bs.getBranditems());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return Collections.EMPTY_LIST;
	}

	private List<SerialItem> getCarSerialList(int brandId) {
		String url = String.format(
				"http://car.api.autohome.com.cn/v1/javascript/seriesbybrand.ashx?_appid=car&brandid=%s&typeid=1&state=0x001f",
				brandId);

		OkHttpClient okHttpClient = new OkHttpClient();
		Request request = new Request.Builder().url(url).build();
		Call call = okHttpClient.newCall(request);
		try {
			Response response = call.execute();
			String body = response.body().string();
			ReturnValue<SerialItems> rv = JsonUtils.deserialize(new TypeReference<ReturnValue<SerialItems>>() {
			}, body);

			if (rv != null) {
				SerialItems bs = rv.getResult();
				return Arrays.asList(bs.getSeriesitems());
				// return Arrays.asList(bs.getSeriesitems()).stream().map(p ->
				// p.getName()).collect(Collectors.toList());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return Collections.EMPTY_LIST;
	}

}
