import React from "react";
import styled from "styled-components";

const Name = styled.b`
  text-align: center;
  font-size: x-large;
`;

// This is a simple username component that just makes a big bold username.
// It also serves as a simple example of how to use styled components.
// Name is a <b> tag with those style sheets applied to it.

export function BigText(props: { text: string }): JSX.Element {
  return (
    <p className="centered">
      <Name>{props.text}</Name>
    </p>
  );
}
