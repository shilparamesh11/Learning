public class DataItem{

private int key;
private String value;

public DataItem(int _key, String _value){
	this.key=_key;
	this.value=_value;
}

public int getKey() {
	return key;
}

public String getValue() {
	return value;
}

public String toString() {
	return "("+getKey()+","+getValue()+")";
}

}
