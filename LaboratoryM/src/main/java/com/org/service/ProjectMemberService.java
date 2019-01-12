package com.org.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.org.pojo.ProjectMember;
import com.org.repository.ProjectMemberRepository;

@Service
public class ProjectMemberService {
	
	@Resource
	private ProjectMemberRepository projectMemberRepository;

	public int insertProjectMembers(List<ProjectMember> projectMember) {
		return projectMemberRepository.insertProjectMembers(projectMember);
	}

	public int updateProjectMemberByUserId(List<ProjectMember> pmList) {
		return projectMemberRepository.updateProjectMemberByUserId(pmList);
	}

	public int deleteProjectMemberByProjectId(long projectId) {
		return projectMemberRepository.deleteProjectMemberByProjectId(projectId);
	}

	public List<ProjectMember> selectProjectMemberByUserId(long userId) {
		return projectMemberRepository.selectProjectMemberByUserId(userId);
	}
}