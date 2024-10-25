import java.util.ArrayList;

public class NBowl{
	private ArrayList<NDice> arrayList = new ArrayList<NDice>();
	public NBowl(){
		
	}
	public void add(NDice dice){
		this.arrayList.add(dice);
	}
	public NDice get(int number){
		return this.arrayList.get(number);
	}
	public int size(){
		return this.arrayList.size();
	}
	public void cast(){
		for(int i = 0; i < this.arrayList.size(); i++){
			NDice dice = this.arrayList.get(i);
			dice.cast();
		}
	}
	public String getFace(int number){
		NDice dice = this.arrayList.get(number);
		return dice.getFace();
	}
}
