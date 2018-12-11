package com.yec.ContactSensor_Demo_NBIoTDevice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.huawei.m2m.cig.tup.modules.protocol_adapter.IProtocolAdapter;

public class ProtocolAdapterImpl implements IProtocolAdapter {

	private static final Logger logger = LoggerFactory.getLogger(ProtocolAdapterImpl.class);
	// 厂商ID
	private static final String MANU_FACTURERID = "ChinaYec";
	// 设备型号
	private static final String MODEL = "NBIoT01";

	@Override
	public String getManufacturerId() {
		return MANU_FACTURERID;
	}

	@Override
	public String getModel() {
		return MODEL;
	}

	public void activate() {
		logger.info("Codec demo HttpMessageHander activated.");
	}

	public void deactivate() {
		logger.info("Codec demo HttpMessageHander deactivated.");
	}

	@Override
	public ObjectNode decode(byte[] binaryData) throws Exception {
		 try {
	            ReportProcess reportProcess = new ReportProcess(binaryData);
	            ObjectNode objectNode = reportProcess.toJsonNode();
	            return objectNode;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	}

	@Override
	public byte[] encode(ObjectNode input) throws Exception {
		try {
			CmdProcess cmdProcess = new CmdProcess(input);
			byte[] byteNode = cmdProcess.toByte();
			return byteNode;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
