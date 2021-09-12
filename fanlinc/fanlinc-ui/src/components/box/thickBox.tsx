import React from "react";
import styled from "styled-components";

const DarkBox = styled.div`
margin-left: 3px
margin-right: 3px
margin-bottom: 3px
border: 1px solid grey
`;

/*
  A styled component consisting of a box with a light shadow around it.
*/
export function ThickBox(props: any): JSX.Element {
  return <DarkBox>{props.children}</DarkBox>;
}
