import cv2
import mediapipe as mp
import json
import sys


def process_video(input_video_path, output_json_path):
    # Initialize MediaPipe Pose
    mp_pose = mp.solutions.pose
    pose = mp_pose.Pose()

    # Open video file
    cap = cv2.VideoCapture(input_video_path)
    print(cap.isOpened())
    sys.stdout.flush()
    # Get video information
    fps = cap.get(cv2.CAP_PROP_FPS)
    frame_width = int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
    frame_height = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))

    # Initialize output data structure
    output_data = []

    # Process each frame
    frame_number = 0

    print("Starting loop...")
    while cap.isOpened():

        ret, frame = cap.read()

        if not ret:
            print("not ret lol")
            break

        # Increment frame number
        frame_number += 1

        # Convert the frame to RGB
        rgb_frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)

        # Process the frame with MediaPipe Pose
        results = pose.process(rgb_frame)

        # Extract 3D pose landmarks
        if results.pose_landmarks:
            landmarks_3d = [(landmark.x, landmark.y, landmark.z) for landmark in results.pose_landmarks.landmark]
        else:
            landmarks_3d = None

        # Add frame data to output
        frame_data = {
            "frame_number": frame_number,
            "landmarks_3d": landmarks_3d
        }
        output_data.append(frame_data)

    # Release video capture and pose estimation resources
    cap.release()
    pose.close()
    print("Done with loop")

    # Write output to JSON file
    print("Opening JSON file")
    with open(output_json_path, 'w') as json_file:
        json.dump(output_data, json_file, indent=2)
    print("DONE!")


cwd = sys.argv[1]

input_path = cwd + "\\res\\temp\\vid.mp4"
# print(input_video_path)
output_path = cwd + "\\res\\temp\\data.json"
try:
    process_video(input_path, output_path)
except Exception:
    print(Exception)
