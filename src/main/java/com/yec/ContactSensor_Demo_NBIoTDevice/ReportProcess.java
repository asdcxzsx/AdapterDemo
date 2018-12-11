package com.yec.ContactSensor_Demo_NBIoTDevice;

import java.util.Random;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 设备对云平台说的话进行的处理
 * 
 * @author lemon
 *
 */
public class ReportProcess {

	private String msgType = "deviceReq";
	private int hasMore = 0;
	private int errcode = 0;

//	private byte noMid = 0x00;
//	private byte hasMid = 0x01;
	private boolean isContainMid = false;
	private int mid = 0;
	private Random ra = new Random();

	public ReportProcess(byte[] binaryData) {
		if (binaryData[0] == 0x00) {
			this.msgType = "deviceReq"; // 数据上报
		} else {
			this.msgType = "deviceRsp"; // 设备响应，就是发送设备命令之后，返回给云端的响应二进制流
			this.mid = ra.nextInt(2000);
			this.isContainMid = true;
//			if (binaryData[4] == hasMid) {
//				mid = Utilty.getInstance().bytes2Int(binaryData, 5, 2);
//				if (Utilty.getInstance().isValidofMid(mid)) {
//					isContainMid = true;
//				}
//			}
		}
	}

	@SuppressWarnings("deprecation")
	public ObjectNode toJsonNode() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			ObjectNode root = mapper.createObjectNode();
			root.put("msgType", this.msgType);
			if (this.msgType.equals("deviceReq")) {
				// 数据上报
				root.put("hasMore", this.hasMore);// 是否有更多数据
				ArrayNode arrynode = mapper.createArrayNode();

				ObjectNode vibrateNode = mapper.createObjectNode();
				vibrateNode.put("serviceId", "Vibrate");// 振动 ,加速度Acceleration,Speed,Displacement,temperature
				ObjectNode vibrateData = mapper.createObjectNode();
				vibrateData.put("Acceleration", Double.parseDouble(String.format("%.2f", ra.nextDouble())));
				vibrateData.put("Speed", ra.nextInt(7) + Double.parseDouble(String.format("%.2f", ra.nextDouble())));
				vibrateData.put("Displacement",
						ra.nextInt(1) + Double.parseDouble(String.format("%.3f", ra.nextDouble())));
				vibrateNode.set("serviceData", vibrateData);

				ObjectNode temperatureNode = mapper.createObjectNode();
				temperatureNode.put("serviceId", "Temperature");
				ObjectNode temperatureData = mapper.createObjectNode();
				temperatureData.put("temperature", Utilty.getRandom(10, 25));
				temperatureNode.put("serviceData", temperatureData);

				arrynode.add(vibrateNode);
				arrynode.add(temperatureNode);
				root.set("data", arrynode);
			} else {
				// 命令响应
				root.put("errcode", this.errcode);
				if (isContainMid) {
					root.put("mid", this.mid);// mid
				}
				ObjectNode body = mapper.createObjectNode();
				body.put("result", 0);
				root.set("body", body);
			}
			return root;
		} catch (Exception e) {
			return null;
		}
	}
}
