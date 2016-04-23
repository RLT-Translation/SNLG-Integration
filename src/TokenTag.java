
public enum TokenTag {

    SUBJ (TokenGroup.TokenType),
    OBJ (TokenGroup.TokenType),
    VERB (TokenGroup.TokenType),
    PREP (TokenGroup.TokenType),
    CONJ (TokenGroup.TokenType),

    PAST (TokenGroup.TenseType),
    PRESENT (TokenGroup.TenseType),
    FUTURE (TokenGroup.TenseType),
    
    SING (TokenGroup.NumberType),
    PLURAL (TokenGroup.NumberType);

    private TokenGroup group;

    TokenTag(TokenGroup group) {
        this.group = group;
    }

    public boolean isInTokenGroup(TokenGroup group) {
        return this.group == group;
    }
    
    public TokenGroup getTokenGroup() {
    	return this.group;
    }

    public enum TokenGroup {
        TokenType,
        TenseType,
        NumberType
    }
}
