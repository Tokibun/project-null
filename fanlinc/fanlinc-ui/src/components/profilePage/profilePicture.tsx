import React from "react";

// This is a simple profile picture component.
// It takes in an image URL and a username (if the image doesnt load it displayes username_Avatar)
// and it centers the image inside the div, and fixes it to 200x200.

export function ProfilePicture(props: {
  profileImage: string;
  username: string;
}): JSX.Element {
  return (
    <img
      style={{
        width: 200,
        height: 200,
        display: "block",
        marginLeft: "auto",
        marginRight: "auto"
      }}
      className="centered"
      src={props.profileImage}
      alt={`${props.username}_Avatar`}
    />
  );
}
