package io.atlasmap.v2;

public class  RecommendationMapping {

    public RecommendationMapping()
    {

    }

    protected Field inputField;
    protected Field outputField;
    protected String recommendationScore;
    public Field getInputField() {
        return inputField;
    }
    public void setInputField(Field inputField) {
        this.inputField = inputField;
    }
    public Field getOutputField() {
        return outputField;
    }
    public void setOutputField(Field outputField) {
        this.outputField = outputField;
    }
  
    public String getRecommendationScore() {
        return recommendationScore;
    }
    public void setRecommendationScore(String recommendationScore) {
        this.recommendationScore = recommendationScore;
    }
    @Override
    public String toString() {
        return "Mapping [AtlasMappingScore=" + recommendationScore + ", inputField=" + inputField + ", outputField="
                + outputField + "]";
    }
    


}