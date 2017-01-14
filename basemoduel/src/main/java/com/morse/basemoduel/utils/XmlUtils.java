package com.morse.basemoduel.utils;

public class XmlUtils {
	
	/**
	 * 解析服务器上下载的文件
	 * 
	 * @param is
	 * @return
	 * @throws Exception
	 */
//	public UpdateInfo getUpdataInfo(InputStream is) throws Exception {
//		XmlPullParser parser = Xml.newPullParser();
//		parser.setInput(is, "UTF-8");
//		int type = parser.getEventType();
//		UpdateInfo info = new UpdateInfo();
//		while (type != XmlPullParser.END_DOCUMENT) {
//			switch (type) {
//			case XmlPullParser.START_TAG:
//				if ("versionCode".equals(parser.getName())) {
//					info.setVersionCode(StringUtils.string2Int(parser.nextText()));
//				} else if ("description".equals(parser.getName())) {
//					info.setDescription(parser.nextText());
//				} else if ("path".equals(parser.getName())) {
//					info.setPath(parser.nextText());
//				}
//				break;
//			}
//			type = parser.next();
//		}
//		return info;
//	}
//
//	public static Map<String, CityArea> getObject(InputStream inputXml) throws Exception {
//		XmlPullParser pullParse = Xml.newPullParser();
//		Map<String, CityArea> objs = null;
//		pullParse.setInput(inputXml, "UTF-8");
//		CityArea cityArea = null;
//		String province = null;
//		int event = pullParse.getEventType();
//		while (event != XmlPullParser.END_DOCUMENT) {
//			switch (event) {
//			case XmlPullParser.START_DOCUMENT:
//				objs = new HashMap<String, CityArea>();
//				break;
//			case XmlPullParser.START_TAG:
//				if ("province".equals(pullParse.getName())) {
//					pullParse.getProperty("name");
//
//					province = pullParse.getAttributeName(0);
//					province = pullParse.getAttributeValue(0);
//				}
//				if ("city".equals(pullParse.getName())) {
//					cityArea = new CityArea();
//					cityArea.setProvince(province);
//				}
//
//				if ("cityname".equals(pullParse.getName())) {
//					String cityname = pullParse.nextText();
//					cityArea.setCityName(cityname);
//				}
//				if ("areacode".equals(pullParse.getName())) {
//					String areacode = pullParse.nextText();
//					cityArea.setAreaCode(areacode);
//				}
//				if ("adDominName".equals(pullParse.getName())) {
//					String adDominName = pullParse.nextText();
//					cityArea.setAdDominName(adDominName);
//				}
//				if ("WIFIauthName".equals(pullParse.getName())) {
//					String WIFIauthName = pullParse.nextText();
//					cityArea.setwIFIauthName(WIFIauthName);
//				}
//				break;
//			case XmlPullParser.END_TAG:
//
//				if ("city".equals(pullParse.getName())) {
//					objs.put(cityArea.getCityName(), cityArea);
//					cityArea = null;
//				}
//				if ("province".equals(pullParse.getName())) {
//					province = null;
//				}
//				break;
//			}
//			event = pullParse.next();
//		}
//		return objs;
//	}
}
