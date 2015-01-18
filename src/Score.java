/*
 * Program: Y-Type
 * Name: Austin Du and Maria Kang
 * Date: June 12, 2014
 * Teacher: Mrs. S
 * Description: Comparing scores
 */

public class Score implements Comparable <Score> {
	int score;
	String name;
	
	public Score(String name, int score){
		if (name.length()!=0){
			this.name=name;
		}else{
			this.name="Anonymous";
		}
		this.score=score;
	}
	
	public int getScore(){
		return score;
	}
	
	public String getName(){
		return name;
	}
	
	public int compareTo(Score a){ //comparing score values, ascending order. If scores are the same, compare name alphabetically instead.
		int scoreB = a.getScore();
		if (this.score!=scoreB){
			return scoreB-this.score;
		}else{
			return a.name.compareTo(this.name);
		}
	}
	
	public String toString(){
		return name+" "+score;
	}
}
