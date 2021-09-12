import React from "react";
import { Box } from "../box/box";

// This is a simple functional component which just displays biography text.
// It centers the text and puts it in a box.

export function Bio(props: { biography: string }): JSX.Element {
  return <Box className="centered">{props.biography}</Box>;
}
