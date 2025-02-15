package com.eblog.demo.chatgpt.model.response;

import java.util.ArrayList;
import java.util.List;

public class PlagReport {
    private String error;
    private int error_code;
    private String message;
    private String text;
    private String percent;
    private List<List<String>> highlight;
    private List<Match> matches;
    private String title;
    private int words_count;
    
    public PlagReport() {
		super();
		// TODO Auto-generated constructor stub
	}

  

	public String getError() {
		return error;
	}







	public void setError(String error) {
		this.error = error;
	}







	public int getError_code() {
		return error_code;
	}







	public void setError_code(int error_code) {
		this.error_code = error_code;
	}







	public String getMessage() {
		return message;
	}







	public void setMessage(String message) {
		this.message = message;
	}







	public String getText() {
		return text;
	}







	public void setText(String text) {
		this.text = text;
	}







	public String getPercent() {
		return percent;
	}







	public void setPercent(String percent) {
		this.percent = percent;
	}







	public List<List<String>> getHighlight() {
		return highlight;
	}







	public void setHighlight(List<List<String>> highlight) {
		this.highlight = highlight;
	}







	public List<Match> getMatches() {
		return matches;
	}







	public void setMatches(List<Match> matches) {
		this.matches = matches;
	}







	public String getTitle() {
		return title;
	}







	public void setTitle(String title) {
		this.title = title;
	}







	public int getWords_count() {
		return words_count;
	}







	public void setWords_count(int words_count) {
		this.words_count = words_count;
	}







	
    public static class Match {
        private String url;
        private String percent;
        private List<List<String>> highlight;
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getPercent() {
			return percent;
		}
		public void setPercent(String percent) {
			this.percent = percent;
		}
		public List<List<String>> getHighlight() {
			return highlight;
		}
		public void setHighlight(List<List<String>> highlight) {
			this.highlight = highlight;
		}
		public Match(String url, String percent, List<List<String>> highlight) {
			super();
			this.url = url;
			this.percent = percent;
			this.highlight = highlight;
		}
		public Match() {
			super();
			// TODO Auto-generated constructor stub
		}
        
        
        
    }
}
