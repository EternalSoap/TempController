package sample;

/**
 * Created by frang on 30-Jan-17.
 */
public class AverageHelper {

    double tempSum;
    int numVals;
    double time;

    public AverageHelper(){
        this.tempSum = 0;
        this.numVals = 0;
    }

    public double average(){
        return this.tempSum/this.numVals;
    }

    public void addTemp (double temp){
        this.tempSum+= temp;
        this.numVals ++;
    }

}
