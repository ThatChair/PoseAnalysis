import cv2
import json
import mediapipe as mp
import sys


def process_video(input_video_path, output_json_path, model_complexity):
    # Initialize MediaPipe Pose
    mp_pose = mp.solutions.pose
    pose = mp_pose.Pose(model_complexity=model_complexity, use_gpu=True)

    # Open video file
    cap = cv2.VideoCapture(input_video_path)

    # Get video information
    fps = cap.get(cv2.CAP_PROP_FPS)
    frame_width = int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
    frame_height = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))

    # Initialize output data structure
    output_data = [fps]

    # Process each frame
    frame_number = 0

    while cap.isOpened():

        ret, frame = cap.read()

        if not ret:
            break

        # Increment frame number
        frame_number += 1

        # Convert the frame to RGB
        rgb_frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)

        # Process the frame with MediaPipe Pose
        results = pose.process(rgb_frame)

        # Extract 3D pose landmarks
        if results.pose_world_landmarks:
            landmarks_3d = [(landmark.x, landmark.y, landmark.z) for landmark in results.pose_world_landmarks.landmark]

            # Remove landmarks with IDs 0-10
            landmarks_3d = landmarks_3d[11:]
        else:
            landmarks_3d = None

        # Add frame data to output
        output_data.append(landmarks_3d)

    # Release video capture and pose estimation resources
    cap.release()
    pose.close()

    # Write output to JSON file
    with open(output_json_path, 'w') as json_file:
        json.dump(output_data, json_file, indent=2)


cwd = sys.argv[1]

input_path = sys.argv[2]

output_path = cwd + "\\res\\temp\\data.json"
try:
    process_video(input_path, output_path, 2)
except Exception:
    print(Exception)
