import React from "react";
import Box from "@mui/material/Box";
import {Button, FormControl, Grid, InputLabel, MenuItem, Select, TextareaAutosize} from "@mui/material";
import NLPPredictorService from "../../service/NLPPredictorService";
import Loader from "../common/Loader";
import ResultGrid from "../common/ResultGrid";

const NLP = () => {
    const [embedding, setEmbedding] = React.useState("TFIDF");
    const [predictionResponse, setPredictionResponse] = React.useState(null);
    const [songLyrics, setSongLyrics] = React.useState(null);
    const [loading, setLoading] = React.useState(false);

    function handleEmbeddingChange(e) {
        setEmbedding(e.target.value)
    }

    function executeNLPClassifier() {
        setLoading(true);
        NLPPredictorService.getPredictionNLP(songLyrics, embedding)
            .then((response) => {
                if (response.success) {
                    setPredictionResponse(response.data);
                    setLoading(false);
                } else {
                    alert("Failed");
                    setLoading(false);
                }
            })
            .catch((ex) => {
                console.log("error");
                setLoading(false);
            });
    }

    function handleSongLyrics(e) {
        setSongLyrics(e.target.value);

    }

    return (
        <>
            {loading ? <Loader/> : null}
            <Box id="nlp-pred" className="nlp-pred">
                <h1>NLP Processor</h1>
            </Box>
            <Box>
                <Grid container>
                    <Grid item xs={12} sm={6} md={3}>
                        <FormControl fullWidth sx={{paddingRight: '40px'}}>
                            <InputLabel id="embedding-lbl">Choose Embedding Technique </InputLabel>
                            <Select
                                labelId="embedding-lbl"
                                id="embedding-select"
                                label="Choose Embedding Algorithm"
                                value={embedding}
                                onChange={handleEmbeddingChange}
                            >
                                <MenuItem value="TFIDF">TFIDF</MenuItem>
                                <MenuItem value="FastText">FastText</MenuItem>
                                <MenuItem value="Transformer">Transformer</MenuItem>
                            </Select>
                        </FormControl>
                    </Grid>
                </Grid>
                <br/>
                <Grid id={'lyrics-setter'} container justifyContent="center">
                    <TextareaAutosize cols={50} minRows={20}
                                      value={songLyrics}
                                      onChange={handleSongLyrics}
                    ></TextareaAutosize>
                </Grid>
                <Grid container justifyContent="flex-end">
                    <Button id="nlp-cluster-button-button" size="medium" variant="contained" color='secondary'
                            style={{marginTop: "20px"}} onClick={() => executeNLPClassifier()}>Predict</Button>
                </Grid>
                <ResultGrid predictionResponse={predictionResponse} type={"nlp"}/>
            </Box>

        </>
    )
}

export default NLP;