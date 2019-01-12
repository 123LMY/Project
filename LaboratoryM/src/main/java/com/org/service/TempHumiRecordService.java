package com.org.service;

import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import com.org.pojo.TempHumiRecord;
import com.org.repository.TempHumiRecordRepository;

@Service
public class TempHumiRecordService {

	@Resource
	private TempHumiRecordRepository tempHumiRecordRepository;
	
	public List<TempHumiRecord> selectTempHumiRecord(String startTime,String endTime){
		return tempHumiRecordRepository.selectTempHumiRecord(startTime, endTime);
	}
	
	public void deleteTemphumirecord() {
		tempHumiRecordRepository.deleteTemphumirecord();
	}
	
	public boolean insertTemphumirecord(String extAddr, char sensorType,String t_h_value,String takeTime) {
		return tempHumiRecordRepository.insertTemphumirecord(extAddr,sensorType, t_h_value, takeTime);
	}
	
	public List<TempHumiRecord> selectHumidity(){
		return tempHumiRecordRepository.selectHumidity();
	}
	
	public List<TempHumiRecord> selectTemperature(){
		return tempHumiRecordRepository.selectTemperature();
	}
}
