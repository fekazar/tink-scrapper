package ru.tinkoff.edu.java.scrapper.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import ru.tinkoff.edu.java.scrapper.repository.pojo.Link;

/*
    This interface is part of service => it can inject only repositories.
 */

public interface LinkProcessor {
    Result process(Link linkRecord);

    class Result {
        @Getter
        @Setter
        private Link linkRecord;
        private boolean hasChanges;

        public Result() {
        }

        public Result(Link linkRecord, String defUpdate) {
            this.linkRecord = linkRecord;
            updates.add(defUpdate);
        }

        private final List<String> updates = new ArrayList<>();

        public String getDescription() {
            if (updates.isEmpty())
                return "No updates.";

            return updates.stream().collect(Collectors.joining("\n")) + "\n\nAt: " + linkRecord.getUrl();
        }

        public void addUpdate(String update) {
            updates.add(update);
        }

        public void setChanged() {
            hasChanges = true;
        }

        public boolean hasChanges() {
            return hasChanges;
        }
    }
}
