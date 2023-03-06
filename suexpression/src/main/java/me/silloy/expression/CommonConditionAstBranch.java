package me.silloy.expression;

import lombok.ToString;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 功能描述: 通用抽象语法树处理器（分支）
 *
 */
@ToString
public class CommonConditionAstBranch implements SyntaxStatement {

    /**
     * 扩展词组列表（如 = -1, > xxx ...）
     */
    protected final List<TokenDescriptor> extendTokens = new ArrayList<>();

    protected TokenTypeEnum tokenType;

    /**
     * 主控词（如   and, my_udf($123)）
     */
    protected TokenDescriptor masterToken;


    public CommonConditionAstBranch(TokenDescriptor masterToken,
                                    Iterator<TokenDescriptor> candidates,
                                    TokenTypeEnum tokenType) {
        this.masterToken = masterToken;
        this.tokenType = tokenType;
        for (int i = 0; i < getFixedExtTokenNum(); i++) {
            if(!candidates.hasNext()) {
                throw new RuntimeException("用法不正确: ["
                        + masterToken.getRawWord() + "] 缺少变量");
            }
            addExtendToken(candidates.next());
        }
    }

    protected void addExtendToken(TokenDescriptor token) {
        extendTokens.add(token);
    }

    @Override
    public String translateTo(TargetDialectTypeEnum targetSqlType) {
        String separator = " ";
        StringBuilder sb = new StringBuilder(masterToken.getRawWord()).append(separator);
        extendTokens.forEach(r -> sb.append(r.getRawWord()).append(separator));
        return sb.toString();
    }

    protected int getFixedExtTokenNum() {
        return 0;
    }

}