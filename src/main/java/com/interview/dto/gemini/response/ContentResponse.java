package com.interview.dto.gemini.response;

import java.util.List;

public class ContentResponse {

    private List<PartResponse> parts;

    public ContentResponse() {
    }

    public List<PartResponse> getParts() {
        return parts;
    }

    public void setParts(List<PartResponse> parts) {
        this.parts = parts;
    }
}