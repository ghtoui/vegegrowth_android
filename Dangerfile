# Ktlintの結果を取得
ktlint_output = File.read("ktlint_report.json")
ktlint_report = JSON.parse(ktlint_output)

# Ktlintの結果から警告を生成
warnings = []

ktlint_report["reports"].each do |report|
  report["errors"].each do |error|
    file_path = report["file"]
    line_number = error["line"]
    error_message = error["message"]

    # 警告を生成
    warnings << "File: #{file_path}, Line: #{line_number}, Error: #{error_message}"
  end
end

# 警告をプルリクエストに追加
if warnings.any?
  warn("Ktlint detected the following issues:")
  warnings.each { |warning| warn(warning) }
end
