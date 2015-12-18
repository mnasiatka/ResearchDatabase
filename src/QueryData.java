public class QueryData {

	String query;

	public QueryData() {
		query = "";
	}

	public QueryData(String key, String value) {
		query = key + "=" + value;
	}

	public String add(String key, String value) {
		if (query.trim().length() > 0) {
			query += "&" + key + "=" + value;
		} else {
			query = key + "=" + value;
		}
		return query;
	}

	public String getQuery() {
		return query;
	}
}
