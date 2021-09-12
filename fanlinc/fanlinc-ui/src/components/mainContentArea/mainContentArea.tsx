import React from "react";
import { mainContentStream$ } from "../../subjects/mainContentArea";
import { Box } from "../box/box";

export class MainContentArea extends React.Component<
  { content?: any },
  { content: any }
> {
  // a simple class that displays whatever it gets fed through the main content stream.

  private contentStream: any;

  public constructor(props: any) {
    super(props);
    this.state = { content: props.content };
  }

  public componentDidMount() {
    this.contentStream = mainContentStream$.subscribe(content => {
      this.setState({ content });
    });
  }

  public componentDidUpdate(prevProps: never, prevState: any) {
    if (prevState.content !== this.state.content) {
      window.scrollTo(0, 0);
    }
  }

  public componentWillUnmount() {
    this.contentStream.unsubscribe();
  }

  public render() {
    return <Box>{this.state.content}</Box>;
  }
}
