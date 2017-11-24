package es.unizar.iaaa.pid.web.rest.vm;

/**
 * View Model object for storing a csv data.
 */
public class CsvData {

    private long namespaceId;

    private String data;

    public CsvData() {
        // Empty public constructor used by Jackson.
    }

    public long getNamespaceId() {
		return namespaceId;
	}



	public void setNamespaceId(long namespaceId) {
		this.namespaceId = namespaceId;
	}



	public String getData() {
		return data;
	}



	public void setData(String data) {
		this.data = data;
	}

	@Override
    public String toString() {
        return "CsvData{" +
            "namespaceId='" + namespaceId + '\'' +
            ", data='" + data + '\'' +
            '}';
    }
}
