package ru.tinkoff.edu.java.scrapper.client.urlprocessor;

import lombok.Getter;
import lombok.Setter;
import ru.tinkoff.edu.java.scrapper.repository.records.LinkRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface UrlProcessor {
    public static final String THREAD_POOL = "url_processor_pool";
    Result process(LinkRecord linkRecord);

    public static class Result {
        @Getter
        @Setter
        private LinkRecord linkRecord;
        private boolean hasChanges;

        public Result() {
        }

        public Result(LinkRecord linkRecord, String defUpdate) {
            this.linkRecord = linkRecord;
            updates.add(defUpdate);
        }

        private final List<String> updates = new ArrayList<>();

        public String getDescription() {
            if (updates.isEmpty())
                return "No updates.";

            return updates.stream().collect(Collectors.joining("\n")) + "\n\nAt: " + linkRecord.url();
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
