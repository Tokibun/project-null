import React from "react";
import { Box } from "./box";

/*
  A styled component consisting of a box but without any borders or shadows.
*/
export function InvisibleBox(props: any): JSX.Element {
  return <Box className="box">{props.children}</Box>;
}
