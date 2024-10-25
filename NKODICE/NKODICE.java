import java.util.Scanner;

public class NKODICE{
	private NBowl bowl = new NBowl();
	private int n_of_roll = 3;
	private int n_of_dice = 5;
	private int n_of_nudge = 5;
	private int n_of_word = 0;
	//総ロール数、総使用ダイス、総ナッジ数、役成立数、役コンボ数、ゾロ目数
	private int[] n_of_result = new int[6];
	//UMCの順に対応する
	private int[] score = new int[3];
	private int total_score = 0;
	private String[] score_name = {"U", "M", "C"};
	private String[] face = {"う","ま","ち","お","こ","ん"};
	private int[] face_point = {500, 500, 500, 300, 100, 50};
	//単語ごとの文字必要数
	private int[][] word = {{1, 0, 1, 0, 0, 1}, //UNC
							{1, 0, 0, 0, 1, 1}, //UNK
							{0, 1, 0, 0, 1, 1}, //MNK
							{0, 0, 1, 0, 1, 1}, //CNK
							{0, 1, 0, 1, 1, 1}, //OMNK
							{0, 0, 2, 0, 0, 2}, //CNCN
							{0, 0, 2, 1, 0, 2}};//OCNCN
	private int[] word_combo = new int[word.length];
	private String[] words = {"UNCHI","UNKO","MANKO","OMANKO","CHINKO","CHINCHIN","OCHINCHIN"};
	private int[] word_point = {1000, 1000, 1000, 5000, 1000, 3000, 100000};
	public NKODICE(){
		//nothing
	}
	public void information(){
		print("これはNKODICEです", 2);
		if(question("簡単な説明を聞きますか？")){
			print("ダイスを振って高得点を目指すゲームです", 2);
			print("お椀を振る（ロール）は基本3回です", 2);
			print("ダイスは基本5つです", 2);
			print("目は、「う」「ま」「ち」「お」「こ」「ん」です", 2);
			print("出目による得点の他に、役の成立による得点やゾロ目倍率ボーナスがあります", 2);
			print("ダイスの目が確定する直前、再度シャッフル（ナッジ）することができます", 2);
			print("ナッジは基本5回までです", 2);
			print("詳しくは資料をご確認ください", 2);
		}
		print("それでは開始します", 1);
	}
	public void game(){
		while(n_of_roll > 0){
			draw_line(20);
			print_status();
			wait_roll();
			roll();
			if(n_of_nudge > 0) nudge();
			judge();
		}
	}
	public void result(){
		draw_line(20);
		print("[結果発表]", 1);
		print("総ロール数：" + n_of_result[0] + " / 総使用ダイス：" + n_of_result[1], 1);
		print("総ナッジ数：" + n_of_result[2] + " / 役成立数：" + n_of_result[3], 1);
		print("役コンボ数：" + n_of_result[4] + " / ゾロ目数：" + n_of_result[5], 1);
		print("", 1);
		print("U：" + score[0] + " / M：" + score[1] + " / C：" + score[2], 0.5f);
		for(int i = 0; i < score.length; i++) total_score += score[i];
		print("総スコア：" + total_score, 1);
	}
	public void print(String str, float sec){
		for(int i = 0; i < str.length(); i++) {
			System.out.print(str.charAt(i));
			sleep(100);
		}
		if(sec != 0){
			System.out.println("");
			sleep(sec * 1000);
		}
		
	}
	public void sleep(float millis){
		try{
			Thread.sleep((long)millis);
		}
		catch(InterruptedException e){
		}
	}
	public boolean question(String str){
		while(true){
			print(str + "（Y/N）：", 0);
			Scanner scanner = new Scanner(System.in);
			String input = scanner.nextLine();
			if(input.equals("Y") || input.equals("y")) return true;
			else if(input.equals("N") || input.equals("n")) return false;
			else print("YかNを入力してください", 1);
		}
	}
	public void print_status(){
		print("残りロール数：" + n_of_roll + " / ダイス数：" + n_of_dice, 1);
		print("U：" + score[0] + " / M：" + score[1] + " / C：" + score[2], 0.5f);
	}
	public void draw_line(int num){
		for(int i = 0; i < num; i++) System.out.print("-");
		System.out.println("");
	}
	public void wait_roll(){
		print("Enterでダイスを振る：", 0);
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();
	}
	public void roll(){
		n_of_roll--;
		n_of_result[0]++;
		n_of_result[1] += n_of_dice;
		print("カランコロン…", 3);
		bowl = new NBowl();
		for(int i = 0; i < n_of_dice; i++) bowl.add(new NDice());
		bowl.cast();
		print_result();
	}
	public void print_result(){
		for(int i = 0; i < bowl.size(); i++){
			print(bowl.getFace(i) + " ", 0);
		}
		print("", 1);
	}
	public void nudge(){
		print("目が決まりそうだ！", 0.2f);
		if(question("ナッジする？（ナッジ可能回数：" + n_of_nudge + "）")){
			n_of_nudge--;
			n_of_result[2]++;
			print("カラン…", 2);
			bowl.cast();
			print_result();
			if(n_of_nudge > 0) nudge();
		}
	}
	public void judge(){
		print("決定！", 1);
		print_result();
		n_of_dice = 5;
		n_of_nudge = 5;
		int[] n_of_face = new int[6];
		//目の数をカウントする
		for(int i = 0; i < bowl.size(); i++){
			for(int j = 0; j < face.length; j++){
				if(bowl.getFace(i).equals(face[j])) n_of_face[j]++;
			}
		}
		//ゾロ目チェック
		for(int i = 0; i < face.length; i++){
			if(n_of_face[i] >= 3){
				n_of_result[5]++;
				print(face[i] + "のゾロ目！（" + n_of_face[i] + "）", 0.5f);
				//倍率を計算
				float scale = 0;
				switch(i){
					case 3:
					case 4:
						scale = 1.5f;
						break;
					case 5:
						scale = -3.0f;
						break;
					default:
						scale = 2.0f;
						break;
				}
				//追加倍率を計算
				if(i == 5) scale -= n_of_face[i] - 3;
				else scale += n_of_face[i] - 3;
				if(scale < -4) scale = -4.0f;
				if(scale > 4) scale = 4.0f;
				switch(i){
					case 3:
					case 4:
					case 5:
						if(i == 3 && Math.signum(score[0]) == -1) scale = -scale;
						for(int j = 0; j < score.length; j++) score[j] *= scale;
						print("現時点でのSDGKスコアが" + scale + "倍されます", 0.5f);
						break;
					//0,1,2
					default:
						score[i] *= scale;
						print("現時点での" + score_name[i] + "スコアが" + scale + "倍されます", 0.5f);
				}
			}
		}

		//出目分の加算
		print("出目に応じた得点が各カテゴリに加算されます", 0.5f);
		for(int i = 0; i < score.length; i++) score[i] += (n_of_face[i] * face_point[i]) + (n_of_face[3] * face_point[3]) + (n_of_face[4] * face_point[4]) + (n_of_face[5] * face_point[5]);
		
		//役のチェック
		check(n_of_face);
		if(n_of_word > 0){
			for(int i = 0; i < words.length; i++){
				if(word_combo[i] > 0){
					n_of_result[3]++;
					print(words[i], 1);
					if(word_combo[i] > 1){
						n_of_result[4]++;
						print("（" + word_combo[i] + "）コンボボーナス！" + pow(2, word_combo[i] - 1) + "倍得点が各カテゴリに加算されます", 0.5f);
					}
					//加算
					switch(i){
						case 0:
						case 1:
							score[0] += word_point[i] + word_point[i] * pow(2, word_combo[i] - 1);
							break;
						case 2:
						case 3:
							score[1] += word_point[i] + word_point[i] * pow(2, word_combo[i] - 1);
							break;
						case 4:
						case 5:
						case 6:
							score[2] += word_point[i] + word_point[i] * pow(2, word_combo[i] - 1);
							break;
					}
				}
			}
			n_of_roll++;
			print("役に応じた得点が各カテゴリに加算されます", 0.5f);
			print("ロール数が1増えます", 0.5f);
			//OCNCN
			if(word_combo[words.length - 1] > 0) n_of_dice = 10;
			else n_of_dice += n_of_word - 1;
			print("次ロールのダイスが" + n_of_dice + "個になります", 0.5f);
			n_of_nudge += n_of_word - 1;
			print("次ロールのナッジが" + n_of_nudge + "回になります", 0.5f);
		}
	}
	public void check(int[] n_of_face){
		boolean[] isContain = new boolean[word.length];
		n_of_word = 0;
		for(int i = 0; i < word.length; i++){
			isContain[i] = true;
			for(int j = 0; j < word[i].length; j++){
				if(n_of_face[j] < word[i][j]) isContain[i] = false;
			}
			if(isContain[i]){
				n_of_word++;
				word_combo[i]++;
				if(word_combo[i] > 4) word_combo[i] = 4;
			}
			else word_combo[i] = 0;
		}
	}
	public int pow(int x, int y){
		int num = 1;
		for(int i = 0; i < y; i++) num *= x;
		if(y < 1) return 0;
		return num;
	}
}
