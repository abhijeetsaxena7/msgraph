package com.microsoft.azure.msalwebsample.config;

import java.util.Set;

public interface AuthScope {
	String baseEndPoint = "https://graph.microsoft.com/";
	
	interface BaseScope{
		public default String getAuthValue() {
			return this.toString().replace("_", ".").toLowerCase();
		}
		
		public default String getReqValue() {
			return new StringBuilder(baseEndPoint).append(this.toString().replace("_", ".")).toString();
		}
		
		
	}
	
	interface User{
		public enum Create implements BaseScope {
			User_ReadWrite_All, Directory_ReadWrite_All, Directory_AccessAsUser_All;
		}

		public enum Update implements BaseScope {
			User_ReadWrite, User_ManageIdentities_All,User_ReadWrite_All, Directory_ReadWrite_All, Directory_AccessAsUser_All;
		}	
		
		public enum Delete implements BaseScope {
			Directory_AccessAsUser_All;
		}
		
		public enum AddRole implements BaseScope{
			AppRoleAssignment_ReadWrite_All, Directory_AccessAsUser_All;
		}
	}
	
	interface Group{
		public enum Create implements BaseScope {
			Group_ReadWrite_All, Directory_ReadWrite_All, Directory_AccessAsUser_All;
		}
		
		public enum Delete implements BaseScope {
			Group_ReadWrite_All, Directory_AccessAsUser_All;
		}
		
		public enum AddOwner implements BaseScope {
			Group_ReadWrite_All, Directory_ReadWrite_All, Directory_AccessAsUser_All;
		}
		
		public enum AddMember implements BaseScope {
			GroupMember_ReadWrite_All, Group_ReadWrite_All, Directory_ReadWrite_All, Directory_AccessAsUser_All;
		}
		
		public enum DeleteOwner implements BaseScope {
			Group_ReadWrite_All, Directory_ReadWrite_All, Directory_AccessAsUser_All;
		}
		
		public enum DeleteMember implements BaseScope {
			GroupMember_ReadWrite_All, Group_ReadWrite_All, Directory_ReadWrite_All, Directory_AccessAsUser_All;
		}
	}
		
	interface Meeting{
		public enum Create implements BaseScope {
			OnlineMeetings_ReadWrite;
		}
		
		public enum Delete implements BaseScope {
			OnlineMeetings_ReadWrite;
		}
		
	}
	

}
