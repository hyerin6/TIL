package net.skhu.dto;

import lombok.Data;

@Data
public class ActionResult {
    boolean success;
    String message;

    public ActionResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
