package me.silloy.expression;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
@ToString
public class ParsedClauseAst {

    private Map<String, Object> idMapping;

    private List<SyntaxStatement> ast;

    public ParsedClauseAst(Map<String, Object> idMapping,
                           List<SyntaxStatement> ast) {
        this.idMapping = idMapping;
        this.ast = ast;
    }

    public Map<String, Object> getidMapping() {
        return idMapping;
    }

    public String translateTo(TargetDialectTypeEnum sqlType) {
        StringBuilder builder = new StringBuilder();
        for (SyntaxStatement tree : ast) {
            builder.append(tree.translateTo(sqlType));
        }
        return builder.toString().trim();
    }

}