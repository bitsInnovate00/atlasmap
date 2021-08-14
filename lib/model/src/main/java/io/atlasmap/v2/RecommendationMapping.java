package io.atlasmap.v2;

public class  RecommendationMapping {

    public RecommendationMapping()
    {

    }

    protected RecommendationField inputField;
    protected RecommendationField outputField;
    protected String recommendationScore;
    public RecommendationField getInputField() {
        return inputField;
    }
    public void setInputField(RecommendationField inputField) {
        this.inputField = inputField;
    }
    public RecommendationField getOutputField() {
        return outputField;
    }
    public void setOutputField(RecommendationField outputField) {
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