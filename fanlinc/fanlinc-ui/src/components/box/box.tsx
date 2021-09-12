import React from "react";
import styled from "styled-components";

const LightBox = styled.div`
  margin-left: 3px;
  margin-right: 3px;
  margin-bottom: 3px;
`;

const ColoredBox = styled.div`
  margin-left: 3px;
  margin-right: 3px;
  margin-bottom: 3px;
  background-color: PeachPuff;
`;

const Colored2Box = styled.div`
  margin-left: 3px;
  margin-right: 3px;
  margin-bottom: 3px;
  background-color: yellow;
`;

/*
  This is a component that just acts as a div with a box around it.
  It can have other components nested inside of it.
*/
export function Box(props: any): JSX.Element {
  return <LightBox className="box">{props.children}</LightBox>;
}

export function PostBox(props: any): JSX.Element {
  return <ColoredBox className="box">{props.children}</ColoredBox>;
}

export function ReplyBox(props: any): JSX.Element {
  return <Colored2Box className="box">{props.children}</Colored2Box>;
}
