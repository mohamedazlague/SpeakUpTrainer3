$ErrorActionPreference = "Stop"
$WorkingDir = (Get-Location).Path
$BuildEnv = Join-Path $WorkingDir ".buildenv"

if (-not (Test-Path $BuildEnv)) {
    New-Item -ItemType Directory -Force -Path $BuildEnv | Out-Null
}

$JdkUrl = "https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.10%2B7/OpenJDK17U-jdk_x64_windows_hotspot_17.0.10_7.zip"
$JdkZip = Join-Path $BuildEnv "jdk.zip"
$JdkDir = Join-Path $BuildEnv "jdk"

$AndroidSdkUrl = "https://dl.google.com/android/repository/commandlinetools-win-11076708_latest.zip"
$AndroidSdkZip = Join-Path $BuildEnv "android_sdk.zip"
$AndroidSdkDir = Join-Path $BuildEnv "android-sdk"
$CmdLineToolsLatest = Join-Path $AndroidSdkDir "cmdline-tools\latest"

$GradleUrl = "https://services.gradle.org/distributions/gradle-8.7-bin.zip"
$GradleZip = Join-Path $BuildEnv "gradle.zip"
$GradleDir = Join-Path $BuildEnv "gradle-8.7"

Write-Host ">>> 1/5 Downloading Dependencies using curl..."

if (-not (Test-Path $JdkDir)) {
    Write-Host "Downloading JDK 17 (this may take a few minutes)..."
    curl.exe -L -C - --retry 10 --retry-delay 5 --retry-all-errors -o "$JdkZip" "$JdkUrl"
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Download failed. Cleaning up and retrying..."
        Remove-Item -Force "$JdkZip" -ErrorAction SilentlyContinue
        curl.exe -L --retry 10 --retry-delay 5 --retry-all-errors -o "$JdkZip" "$JdkUrl"
    }
    Write-Host "Extracting JDK..."
    Expand-Archive -Path $JdkZip -DestinationPath $BuildEnv -Force
    $ExtractedJdk = Get-ChildItem -Path $BuildEnv -Directory -Filter "jdk-17*" | Select-Object -First 1
    Rename-Item -Path $ExtractedJdk.FullName -NewName "jdk"
}

if (-not (Test-Path $CmdLineToolsLatest)) {
    Write-Host "Downloading Android SDK..."
    curl.exe -L -C - --retry 10 --retry-delay 5 --retry-all-errors -o "$AndroidSdkZip" "$AndroidSdkUrl"
    Write-Host "Extracting Android SDK..."
    Expand-Archive -Path $AndroidSdkZip -DestinationPath (Join-Path $AndroidSdkDir "cmdline-tools") -Force
    Rename-Item -Path (Join-Path $AndroidSdkDir "cmdline-tools\cmdline-tools") -NewName "latest"
}

if (-not (Test-Path $GradleDir)) {
    Write-Host "Downloading Gradle 8.7..."
    curl.exe -L -C - --retry 10 --retry-delay 5 --retry-all-errors -o "$GradleZip" "$GradleUrl"
    Write-Host "Extracting Gradle..."
    Expand-Archive -Path $GradleZip -DestinationPath $BuildEnv -Force
}

Write-Host ">>> 2/5 Setting Environment Variables..."
$env:JAVA_HOME = "$WorkingDir\.buildenv\jdk"
$env:ANDROID_HOME = "$WorkingDir\.buildenv\android-sdk"
$env:PATH = "$env:JAVA_HOME\bin;$env:ANDROID_HOME\cmdline-tools\latest\bin;$WorkingDir\.buildenv\gradle-8.7\bin;" + $env:PATH

Write-Host ">>> 3/5 Accepting Android SDK Licenses and Installing Platforms..."
$yes = "y`n" * 20
$yes | sdkmanager.bat --licenses > $null
sdkmanager.bat "platforms;android-35" "build-tools;35.0.0" "platform-tools" > $null

Write-Host ">>> 4/5 Building APK using Gradle..."
Set-Location $WorkingDir
gradle.bat assembleDebug

Write-Host ">>> 5/5 Done!"
if (Test-Path "app\build\outputs\apk\debug\app-debug.apk") {
    Write-Host "========================================="
    Write-Host "✅ APK Generated Successfully!"
    Write-Host "Location: $(Join-Path $WorkingDir "app\build\outputs\apk\debug\app-debug.apk")"
    Write-Host "========================================="
} else {
    Write-Host "❌ Failed to generate APK."
}
