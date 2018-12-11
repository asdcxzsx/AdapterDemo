package com.yec.ContactSensor_Demo_NBIoTDevice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class CmdProcess {

	private String msgType = "cloudReq";
	private String serviceId = "SleepTime";
	private String cmd = "SET_SLEEP_TIME";

	private int hasMore = 0;
	private int errcode = 0;
	private int mid = 0;
	private JsonNode paras;

	public CmdProcess() {
	}

	public CmdProcess(ObjectNode input) {
		try {
			this.msgType = input.get("msgType").asText();
			if (msgType.equals("cloudRsp")) {
				// 在此组装ACK的值
				this.errcode = input.get("errcode").asInt();
				this.hasMore = input.get("hasMore").asInt();
			} else {
				// 平台下发的请求 --cloudReq
				this.serviceId = input.get("serviceId").asText();
				this.cmd = input.get("cmd").asText();
				if (input.get("method") != null) {
					this.cmd = input.get("method").asText();
				}
				this.paras = input.get("paras");
				this.hasMore = input.get("hasMore").asInt();
				if (input.get("mid") != null) {
					this.mid = input.get("mid").intValue();
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public byte[] toByte() {
		try {
			if (this.msgType.equals("cloudReq")) {
				// 平台下发的请求
				if (this.serviceId.equals("SleepTime")) {
					if (this.cmd.equals("SET_SLEEP_TIME")) {
						int time = paras.get("value").asInt();
						byte[] byteRead = new byte[5];
						ByteBufUtils buf = new ByteBufUtils(byteRead);
						buf.writeByte((byte) 0xAA);
						buf.writeByte((byte) 0x72);
						buf.writeByte((byte) time);
						// 此处需要考虑兼容性，如果没有传mId，则不对其进行编码
						if (Utilty.getInstance().isValidofMid(mid)) {
							byte[] byteMid = new byte[2];
							byteMid = Utilty.getInstance().int2Bytes(mid, 2);
							buf.writeByte(byteMid[0]);
							buf.writeByte(byteMid[1]);
						}
						return byteRead;
					}
				}
			} else {
				// cloudRsp,表示平台收到设备的数据后对设备的应答
				byte[] ack = new byte[4];
				ByteBufUtils buf = new ByteBufUtils(ack);
				buf.writeByte((byte) 0xAA);
				buf.writeByte((byte) 0xAA);
				buf.writeByte((byte) this.errcode);
				buf.writeByte((byte) this.hasMore);
				return ack;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
