package com.api.central.util;

public class MasterDatas {
	
	public static String getAuditTypeDesc(Integer auditTypeId){
		
		//Added by sudharsan for INCIDENT TICKET-684 Start here 
		switch(auditTypeId){
			case AppConstant.ISM_TYPE_ID:
				return "ISM";
			case AppConstant.ISPS_TYPE_Id:
				return "ISSC";
			case AppConstant.MLC_TYPE_ID:
				return "MLC";
			case AppConstant.SSP_TYPE_ID:
				return "SSP";
			case AppConstant.DMLC_TYPE_ID:
				return "DMLC II";
			case AppConstant.IHM_TYPE_ID:
				return "IHM";
			case AppConstant.SOPEP_TYPE_ID:
				return "SOPEP";
			case AppConstant.STS_TYPE_ID:
				return "STS";
			case AppConstant.SMPEP_TYPE_ID:
				return "SMPEP";
			case AppConstant.BWS_TYPE_ID:
				return "BWS";
			case AppConstant.VOC_TYPE_ID:
				return "VOC";
			case AppConstant.SDR_TYPE_ID:
				return "SDR";
			case AppConstant.COW_TYPE_ID:
				return "COW";
			default:
				return null;
		}
		//Added by sudharsan for INCIDENT TICKET-684 End here
	
	}
	
	public static String getAuditSubTypeDesc(Integer auditTypeId, Integer auditSubTypeId){
		
		if((auditTypeId == AppConstant.ISM_TYPE_ID || auditTypeId == AppConstant.ISPS_TYPE_Id || auditTypeId == AppConstant.MLC_TYPE_ID) && auditSubTypeId == AppConstant.INTERIM_SUB_TYPE_ID){
			return "INTERIM";
		}
		if((auditTypeId == AppConstant.ISM_TYPE_ID || auditTypeId == AppConstant.ISPS_TYPE_Id || auditTypeId == AppConstant.MLC_TYPE_ID) && auditSubTypeId == AppConstant.INITIAL_SUB_TYPE_ID){
			return "INITIAL";
		}
		if((auditTypeId == AppConstant.ISM_TYPE_ID || auditTypeId == AppConstant.ISPS_TYPE_Id || auditTypeId == AppConstant.MLC_TYPE_ID) && auditSubTypeId == AppConstant.INTERMEDIATE_SUB_TYPE_ID){
			return "INTERMEDIATE";
		}
		if((auditTypeId == AppConstant.ISM_TYPE_ID || auditTypeId == AppConstant.ISPS_TYPE_Id || auditTypeId == AppConstant.MLC_TYPE_ID) && auditSubTypeId == AppConstant.RENEWAL_SUB_TYPE_ID){
			return "RENEWAL";
		}
		if((auditTypeId == AppConstant.ISM_TYPE_ID || auditTypeId == AppConstant.ISPS_TYPE_Id || auditTypeId == AppConstant.MLC_TYPE_ID) && auditSubTypeId == AppConstant.ADDITIONAL_SUB_TYPE_ID){
			return "ADDITIONAL";
		}
		if((auditTypeId == AppConstant.SSP_TYPE_ID || auditTypeId == AppConstant.DMLC_TYPE_ID || auditTypeId == AppConstant.IHM_TYPE_ID || auditTypeId == AppConstant.SOPEP_TYPE_ID || auditTypeId == AppConstant.STS_TYPE_ID || auditTypeId == AppConstant.SMPEP_TYPE_ID || auditTypeId == AppConstant.BWS_TYPE_ID
				|| auditTypeId == AppConstant.SDR_TYPE_ID || auditTypeId == AppConstant.COW_TYPE_ID || auditTypeId == AppConstant.VOC_TYPE_ID) && auditSubTypeId == AppConstant.SSP_INITIAL_AUD_SUBTYPEID){  //Added by sudharsan for INCIDENT TICKET-684 Start here
			return "INITIAL"; 
		}
		if((auditTypeId == AppConstant.SSP_TYPE_ID || auditTypeId == AppConstant.DMLC_TYPE_ID || auditTypeId == AppConstant.IHM_TYPE_ID|| auditTypeId == AppConstant.SOPEP_TYPE_ID || auditTypeId == AppConstant.STS_TYPE_ID || auditTypeId == AppConstant.SMPEP_TYPE_ID || auditTypeId == AppConstant.BWS_TYPE_ID
				|| auditTypeId == AppConstant.SDR_TYPE_ID || auditTypeId == AppConstant.COW_TYPE_ID || auditTypeId == AppConstant.VOC_TYPE_ID) && auditSubTypeId == AppConstant.AMENDMENT_SUB_TYPE_ID){  //Added by sudharsan for INCIDENT TICKET-684 Start here
			return "AMENDMENT";
		}
		
		return null;
	}
	
	public static String getAuditStatusDesc(Integer auditTypeId, Integer auditStatusId){
		
		if(auditStatusId == AppConstant.COMMENCED_AUDIT_STATUS){  //Added by sudharsan for Jira-ID = IRI-5698 
			return "COMMENCED";
		}
		if(auditStatusId == AppConstant.COMPLETED_AUDIT_STATUS){
			return "COMPLETED";
		}
		if(auditStatusId == AppConstant.CLOSED_AUDIT_STATUS){
			return "CLOSED";
		}
		if(auditStatusId == AppConstant.VOID_AUDIT_STATUS){
			return "VOID";
		}
		if(auditStatusId == AppConstant.REOPEN_AUDIT_STATUS){
			return "REOPEN";
		}
		
		return null;
	}
	
	public static String getCertificateIssueType(Integer certificateIssuedId , Integer auditTypeId ,Integer roleId ){
		
		String certIssueType = "";
		
		if(roleId!=null){
		
			if(certificateIssuedId == AppConstant.INTERIM_CERT){
				certIssueType = "INTERIM";
			}else if(certificateIssuedId == AppConstant.FULL_TERM_CERT){
				certIssueType = "FULL TERM";
			}else if(certificateIssuedId == AppConstant.EXTENSION){
				certIssueType = "EXTENSION";
			}else if(certificateIssuedId == AppConstant.INTERMEDAITE_ENDORSED){
				certIssueType = "INTERMEDAITE ENDORSED";
			}else if(certificateIssuedId == AppConstant.ADDITIONAL_ENDORSED){
				certIssueType = "ADDITIONAL ENDORSED";
			}else if(certificateIssuedId == AppConstant.RENEWAL_ENDORSED1 && roleId==AppConstant.AUDIT_AUDITOR_ROLE_ID){
				certIssueType = "RENEWAL ENDORSED(ISM PART B:13:12-13:14/ISPS 19.3.5-19.3.6)";
			    if(auditTypeId==AppConstant.ISM_TYPE_ID){
			    	
			    	certIssueType =  "RENEWAL ENDORSED(ISM PART B:13:13)";
			    }else if(auditTypeId==AppConstant.ISPS_TYPE_Id){
			    	certIssueType = "RENEWAL ENDORSED(ISM PART B:19.3.4)";
			    }
			    else if (auditTypeId==AppConstant.IHM_TYPE_ID) {
			    	certIssueType = "RENEWAL ENDORSED (11.7 applies)";
			    }else{
			    
			    	certIssueType = "RENEWAL ENDORSED(MLC Appendix A5-II)";
			    }
			
			}else if(certificateIssuedId == AppConstant.RENEWAL_ENDORSED1 && roleId==AppConstant.MANAGER_ROLE_ID){
				certIssueType = "RENEWAL ENDORSED(ISM PART B:13:12-13:14/ISPS 19.3.5-19.3.6)";
			   
				if(auditTypeId==AppConstant.ISM_TYPE_ID){
			    	certIssueType =  "RENEWAL ENDORSED(ISM PART B:13:12-13:14)";
			    }else if(auditTypeId==AppConstant.ISPS_TYPE_Id){
			    	certIssueType = "RENEWAL ENDORSED(ISM PART B:19.3.5-19.3.6)";
			    } else if (auditTypeId==AppConstant.IHM_TYPE_ID) {
			    	certIssueType = "RENEWAL ENDORSED (11.7 applies)";
			    }else{
			    	certIssueType = "RENEWAL ENDORSED(MLC Appendix A5-II)";
			    }
			
			}else if(certificateIssuedId == AppConstant.RENEWAL_ENDORSED2  && roleId==AppConstant.AUDIT_AUDITOR_ROLE_ID){
				certIssueType = "RENEWAL ENDORSED(ISM PART B:13:12-13:14/ISPS 19.3.5-19.3.6)";
			    if(auditTypeId==AppConstant.ISM_TYPE_ID){
			    	System.out.println("curso here coming ");
			    	certIssueType =  "RENEWAL ENDORSED(ISM PART B:13:13)";
			    }else if(auditTypeId==AppConstant.ISPS_TYPE_Id){
			    	certIssueType = "RENEWAL ENDORSED(ISM PART B:19.3.4)";
			    } else if (auditTypeId==AppConstant.IHM_TYPE_ID) {
			    	certIssueType = "RENEWAL ENDORSED (11.8 or 11.9 applies)";
			    }else{
			    	certIssueType = "RENEWAL ENDORSED(MLC Appendix A5-II)";
			    }
			
			}else if(certificateIssuedId == AppConstant.RENEWAL_ENDORSED2  && roleId==AppConstant.MANAGER_ROLE_ID){
				certIssueType = "RENEWAL ENDORSED(ISM PART B:13:12-13:14/ISPS 19.3.5-19.3.6)";
			   
				if(auditTypeId==AppConstant.ISM_TYPE_ID){
			    	certIssueType =  "RENEWAL ENDORSED(ISM PART B:13:12-13:14)";
			    }else if(auditTypeId==AppConstant.ISPS_TYPE_Id){
			    	certIssueType = "RENEWAL ENDORSED(ISM PART B:19.3.5-19.3.6)";
			    }else if (auditTypeId==AppConstant.IHM_TYPE_ID) {
			    	certIssueType = "RENEWAL ENDORSED (11.8 or 11.9 applies)";
			    }else{
			    	certIssueType = "RENEWAL ENDORSED(MLC Appendix A5-II)";
			    }
			
			}
			else if(certificateIssuedId == AppConstant.RE_ISSUE){
				certIssueType = "REISSUE / ADMINISTRATIVE";
			}
			
		}else if(certificateIssuedId == AppConstant.INTERIM_CERT){
			certIssueType = "INTERIM";
		}else if(certificateIssuedId == AppConstant.FULL_TERM_CERT){
			certIssueType = "FULL TERM";
		}else if(certificateIssuedId == AppConstant.EXTENSION){
			certIssueType = "EXTENSION";
		}else if(certificateIssuedId == AppConstant.INTERMEDAITE_ENDORSED){
			certIssueType = "INTERMEDAITE ENDORSED";
		}else if(certificateIssuedId == AppConstant.ADDITIONAL_ENDORSED){
			certIssueType = "ADDITIONAL ENDORSED";
		}else if(certificateIssuedId == AppConstant.RENEWAL_ENDORSED1){
			certIssueType = "RENEWAL ENDORSED(ISM PART B:13:12-13:14/ISPS 19.3.5-19.3.6)";
		}else if(certificateIssuedId == AppConstant.RENEWAL_ENDORSED2){
			certIssueType = "RENEWAL  ENDORSED(ISM PART B:13:12-13:14/ISPS 19.3.5-19.3.6)";
		}else if(certificateIssuedId == AppConstant.RE_ISSUE){
			certIssueType = "REISSUE / ADMINISTRATIVE";
		}
		
		if(auditTypeId==AppConstant.IHM_TYPE_ID) {
			
		 if(certificateIssuedId == AppConstant.FULL_TERM_CERT){
				certIssueType = "FULL TERM";
			}else if(certificateIssuedId == AppConstant.EXTENSION){
				certIssueType = "EXTENDED (11.6 applies)";
			}else if(certificateIssuedId == AppConstant.ADDITIONAL_ENDORSED){
				certIssueType = "ADDITIONAL ENDORSED";
			}else if(certificateIssuedId == AppConstant.RENEWAL_ENDORSED1){
				certIssueType =  "RENEWAL ENDORSED (11.7 applies)";
			}else if(certificateIssuedId == AppConstant.RENEWAL_ENDORSED2){
				certIssueType = "RENEWAL ENDORSED (11.8 or 11.9 applies)";
			}else if(certificateIssuedId == AppConstant.RE_ISSUE){
				certIssueType = "REISSUE / ADMINISTRATIVE";
			}
		}
		return certIssueType;
	}
		
	

	

}
