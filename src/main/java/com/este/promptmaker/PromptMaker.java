package com.este.promptmaker;

import java.util.List;

public class PromptMaker {

    private String prompt;
    private String text;
    private List<String> sources;
    private List<String> shorthands;
    private String details;
    private String submitter;
    private List<String> tags;

    public PromptMaker() {
    }

    public PromptMaker(String prompt, String text, List<String> sources, List<String> shorthands, String detail, String submitter, List<String> tags) {
        this(prompt, sources, shorthands, detail, submitter, tags);
        this.text = text;
    }

    public PromptMaker(String prompt, List<String> sources, List<String> shorthands, String detail, String submitter, List<String> tags) {
        this.prompt = prompt;
        this.sources = sources;
        this.shorthands = shorthands;
        this.details = detail;
        this.submitter = submitter;
        this.tags = tags;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getText() {
        return text;
    }

    public List<String> getSources() {
        return sources;
    }

    public List<String> getShorthands() {
        return shorthands;
    }

    public String getDetails() {
        return details;
    }

    public String getSubmitter() {
        return submitter;
    }

    public List<String> getTags() {
        return tags;
    }

    private String append(List<String> list) {
        StringBuilder sb = new StringBuilder();
        int cnt = 1;
        for (String s : list) {
            sb.append("\"");
            sb.append(s);
            sb.append("\"");
            if (cnt < list.size()) {
                sb.append(",\n    ");
            }
            cnt++;
        }
        return sb.toString();
    }

    public String printText() {
        if (getText() == null){
            return null;
        }
        return "\"" + getText() + "\"";
    }

    public String printList(List<String> list) {
        if (list.isEmpty()) {
            return "\"\"";
        }
        return append(list);
    }

    public String printShorthand() {
        if (getShorthands().isEmpty()) {
            return "[]";
        }
        return "[\n    " + append(getShorthands()) + "\n  ]";
    }

    public String save() {
        return "{\n  \"prompt\": \"" + getPrompt()
                + "\",\n  \"text\": " + printText()
                + ",\n  \"source\": [\n    " + printList(getSources())
                + "\n  ],\n  \"shorthand\": " + printShorthand()
                + ",\n  \"details\": \"" + getDetails()
                + "\",\n  \"submitter\": \"" + getSubmitter()
                + "\",\n  \"tags\": [\n    " + printList(getTags())
                + "\n  ]\n}";
    }
}
