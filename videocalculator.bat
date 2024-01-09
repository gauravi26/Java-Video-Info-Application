@echo off
setlocal enabledelayedexpansion

:mainLoop
REM Prompt user for the path to the folder containing video files
set /p "folder_path=Enter the path to the folder containing video files (type 'exit' to end): "

REM Check if the user wants to exit
if /i "%folder_path%"=="exit" (
    echo Exiting the script.
    goto :eof
)

REM Get the current date and time
for /f "delims=" %%a in ('wmic OS Get localdatetime ^| find "."') do set datetime=%%a
set "day=!datetime:~6,2!"
set "month=!datetime:~4,2!"

set "year=!datetime:~0,4!"

set "hour=!datetime:~8,2!"
set "minute=!datetime:~10,2!"
set "second=!datetime:~12,2!"

REM Set the path to the output file
set "output_file=%folder_path%\output.txt"

REM Set the formatted date and time
set "formatted_datetime=!day!_!month!_!year!_!hour!.!minute!.!second!"

REM Set the path to the CSV file with formatted date and time
set "csv_file=%folder_path%\Information_!formatted_datetime!.csv"

REM Delete existing CSV file
if exist "!csv_file!" del "!csv_file!"

REM Set the path to the FFmpeg executable (using a relative path)
set "ffmpeg_path="%~dp0ffmpeg\bin\ffmpeg.exe""

REM Check if ffmpeg.exe is found in the same directory as the batch file
if not exist "!ffmpeg_path!" (
    echo Error: ffmpeg.exe not found in the same directory as the batch file.
    echo Please make sure ffmpeg.exe is in the same directory as the batch file.
    echo Batch file directory: "!batch_dir!"
    pause
    exit /b 1
)

REM Initialize serial number
set "serial=0"

REM Run ffmpeg command for each video file in the folder
for %%i in ("%folder_path%\*.mp4") do (
    REM Increment serial number
    set /a "serial+=1"

    REM Run ffmpeg command for the video file and append the output to the file
    "!ffmpeg_path!" -i "%%i" 2>&1 | find "Duration" >> "!output_file!"

    REM Extract video file name from the path
    for %%j in ("%%i") do set "video_file_name=%%~nxj"

    REM Extract duration from the output file
    set "duration="
    for /f "tokens=*" %%k in ('findstr /c:"Duration" "!output_file!"') do set "duration=%%k"

    REM Display a message
    echo Video File Name: !video_file_name! saved in "!output_file!"

    REM Check if duration is found and display
    if defined duration (
        REM Extracting only the time part of the duration and removing milliseconds
        for /f "tokens=2 delims=, " %%a in ("!duration!") do set "duration=%%a"

        REM Splitting the duration into hours, minutes, and seconds
        set "hours=!duration:~0,2!"
        set "minutes=!duration:~3,2!"
        set "seconds=!duration:~6,2!"

        REM Adding leading zeros if needed
        if !hours! lss 10 set "hours=!hours!"
        if !minutes! lss 10 set "minutes=!minutes!"
        if !seconds! lss 10 set "seconds=!seconds!"
REM Display the file name, duration, and serial number
echo !serial!,!video_file_name!, !hours!:!minutes!:!seconds!

REM Save to CSV file
if not exist "!csv_file!" (
    (
        echo sr_no,video_file_name,duration
        echo !serial!,"!video_file_name!",!hours!:!minutes!:!seconds!
    ) > "!csv_file!"
) else (
    echo !serial!,"!video_file_name!",!hours!:!minutes!:!seconds! >> "!csv_file!"
)

       ) else (
        echo Duration information not found in output.
    )

    REM Clear the contents of the output file
    echo. > "!output_file!"
)

REM Display the total number of videos encoded
echo Total number of videos encoded: !serial!

REM Go back to the main loop
goto :mainLoop

:eof
REM No longer require a key press to continue
pause
