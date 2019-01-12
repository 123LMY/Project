package com.org.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.org.pojo.Notice;
import com.org.repository.NoticeRepository;

@Service
public class NoticeService {

	@Resource
	private NoticeRepository noticeRepository;

	// 获取公告
	public List selectNotice() {
		List<Notice> noticeList = noticeRepository.selectNotice();
		List<Map> list = new ArrayList<Map>();
		Map map = null;
		for (int i = 0; i < noticeList.size(); i++) {
			map = new HashMap<String, String>();
			map.put("id", noticeList.get(i).getId());
			map.put("operator", noticeList.get(i).getOperator().getRealName());
			map.put("typeName", noticeList.get(i).getTypeName());
			map.put("description", noticeList.get(i).getDescription());
			map.put("publishers", noticeList.get(i).getPublishers().getRealName());
			map.put("publishDate", noticeList.get(i).getPublishDate());
			list.add(map);
		}
		return list;

	}

	// 修改公告
	@Transactional
	public void updateNotice(Notice notice) {
		noticeRepository.updateNotice(notice);
	}

	// 删除公告
	@Transactional
	public void deleteNotices(int[] ids) {
		noticeRepository.deleteNotices(ids);
	}

	// 新增公告
	@Transactional
	public void insertNotice(Notice notice) {
		noticeRepository.insertNotice(notice);
	}

	// 获取前十条公告
	public List selectNoticelimit() {
		List<Notice> noticeList = noticeRepository.selectNoticelimit();
		List<Map> list = new ArrayList<Map>();
		Map map = null;
		for (int i = 0; i < noticeList.size(); i++) {
			map = new HashMap<String, String>();
			map.put("id", noticeList.get(i).getId());
			map.put("operator", noticeList.get(i).getOperator().getRealName());
			map.put("typeName", noticeList.get(i).getTypeName());
			map.put("description", noticeList.get(i).getDescription());
			map.put("publishers", noticeList.get(i).getPublishers().getRealName());
			map.put("publishDate", noticeList.get(i).getPublishDate());
			list.add(map);
		}
		return list;
	}

	@Transactional
	public Map<String, Object> selectNoticeByPage(Map<String, Object> map) {
		Map<String, Object> noticeMap = new HashMap<String, Object>();
		int count = noticeRepository.selectNoticeCount();
		List<Notice> noticeList = noticeRepository.selectNoticeByPage(map);
		noticeMap.put("count", count);
		noticeMap.put("noticeList", noticeList);
		return noticeMap;
	}
}
