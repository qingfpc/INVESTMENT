package org.example.investment;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

/**
 * 将每日分析结果追加到定投分析记录.md，
 * 同一天多次运行会覆盖当天的记录而非重复追加。
 */
public final class ReportStore {

    private static final Path FILE = Paths.get("定投分析记录.md");

    public static void save(LocalDate date, String content) throws IOException {
        String header = "# 定投分析记录\n\n";
        String section = "## " + date + "\n\n" + content.trim() + "\n";

        if (!Files.exists(FILE)) {
            Files.writeString(FILE, header + section, StandardCharsets.UTF_8);
            return;
        }

        String existing = Files.readString(FILE, StandardCharsets.UTF_8);
        String marker = "\n## " + date + "\n";
        int pos = existing.indexOf(marker);

        if (pos >= 0) {
            // 找到同一天的已有记录并替换
            int end = existing.indexOf("\n---\n", pos + 1);
            if (end < 0) end = existing.length();
            existing = existing.substring(0, pos) + "\n" + section + existing.substring(end);
        } else {
            // 追加新记录
            if (!existing.endsWith("\n")) existing += "\n";
            existing += "---\n\n" + section;
        }

        Files.writeString(FILE, existing, StandardCharsets.UTF_8);
    }

    private ReportStore() {}
}
