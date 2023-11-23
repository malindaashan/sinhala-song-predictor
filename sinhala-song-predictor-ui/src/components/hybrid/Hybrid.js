import React from "react";
import Box from "@mui/material/Box";
import {Button, FormControl, Grid, InputLabel, MenuItem, Select, TextareaAutosize} from "@mui/material";
import SongUploader from "../common/SongUploader";
import Loader from "../common/Loader";
import HybridPredictorService from "../../service/HybridPredictorService";

const Hybrid = () => {
    const [selectedAlgorithm, setSelectedAlgorithm] = React.useState("Random-Forest")
    const [selectedFile, setSelectedFile] = React.useState();
    const [predictionResponse, setPredictionResponse] = React.useState(null);
    const [loading, setLoading] = React.useState(false);
    const [embedding, setEmbedding] = React.useState("Transformer");
    const [songLyrics, setSongLyrics] = React.useState(null);

    function handleEmbeddingChange(e) {
        setEmbedding(e.target.value)
    }

    function handleSongLyrics(e) {
        setSongLyrics(e.target.value);

    }

    const handleClassifierAlgoChange = (event) => {
        setSelectedAlgorithm(event.target.value)
        setPredictionResponse(null);
    }

    function executeHybridClassifier() {
        setLoading(true);
        HybridPredictorService.executeHybridClassifier(selectedAlgorithm, selectedFile, songLyrics, embedding)
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

    return (
        <>
            {loading ? <Loader/> : null}
            <Box id="ml-predictor" className="ml-box">
                <h1 justifyContent="center"> Hybrid-Classifier</h1>
                <br/><br/><br/>
                <Grid container>
                    <Grid item xs={12} sm={6} md={3}>
                        <FormControl fullWidth sx={{paddingRight: '40px'}}>
                            <InputLabel id="cluster-lbl">Choose Classifier Algorithm</InputLabel>
                            <Select
                                labelId="classifier-lbl"
                                id="classifier-select"
                                label="Choose Classifier Algorithm"
                                value={selectedAlgorithm}
                                onChange={handleClassifierAlgoChange}
                                disabled={true}
                            >
                                <MenuItem value="SMO">SMO</MenuItem>
                                <MenuItem value="Naive-Bayes">Naive-Bayes</MenuItem>
                                <MenuItem value="Random-Forest">Random-Forest</MenuItem>
                            </Select>
                        </FormControl>
                    </Grid>
                </Grid>
                <br/>
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
                                disabled={true}
                            >
                                <MenuItem value="TFIDF">TFIDF</MenuItem>
                                <MenuItem value="WORD2VEC">WORD2VEC</MenuItem>
                                <MenuItem value="Transformer">Transformer</MenuItem>
                            </Select>
                        </FormControl>
                    </Grid>
                </Grid>
                <br/>
                <Grid id={'song-uploader'} container justifyContent="center">
                    <Grid item xs={12} sm={6} md={3}>
                        <SongUploader
                            setSelectedFile={setSelectedFile}
                        />
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
                    <Button id="nybrid-pred-button" size="medium" variant="contained" color='secondary'
                            style={{marginTop: "20px"}} onClick={() => executeHybridClassifier()}>CLASSIFY</Button>
                </Grid>
                {predictionResponse != null ?
                    <HybridResultGrid resultArray={predictionResponse}/> : null}
            </Box>
        </>
    )

}

const HybridResultGrid = ({resultArray}) => {
    const resultML = resultArray[0];
    const resultNLP = resultArray[1];
    return (
        <>
            <Grid container spacing={1}>
                <Grid item xs={12} sm={6} md={6} justifyContent="left">
                    <table>
                        <tr>
                            <th colSpan={3}>
                                <h1>Result Machine Learning Predictor : {resultML != null ?
                                    resultML.predictedValue === 0 ? "Calm" : resultML.predictedValue === 1 ? "Happy" :
                                        resultML.predictedValue === 2 ? "Sad" : resultML : null}</h1>
                            </th>
                        </tr>

                        <tr>
                            <th colSpan={3}>
                                <h1>Distribution</h1>
                            </th>
                        </tr>
                        <tr>
                            <th>Calm</th>
                            <th>Happy</th>
                            <th>Sad</th>
                        </tr>
                        <tr>
                            <td>{resultML.predictedDistribution.Calm}</td>
                            <td>{resultML.predictedDistribution.Happy}</td>
                            <td>{resultML.predictedDistribution.Sad}</td>

                        </tr>
                    </table>

                </Grid>

                <Grid item xs={12} sm={6} md={6} justifyContent="right">
                    <table>
                        <tr>
                            <th colSpan={3}>
                                <h1>Result Natural Language Predictor : {resultNLP != null ?
                                    resultNLP.predictedValue === 0 ? "Calm" : resultNLP.predictedValue === 1 ? "Happy" :
                                        resultNLP.predictedValue === 2 ? "Sad" : resultNLP : null}</h1>
                            </th>
                        </tr>

                        <tr>
                            <th colSpan={3}>
                                <h1>Distribution</h1>
                            </th>
                        </tr>
                        <tr>
                            <th>Calm</th>
                            <th>Happy</th>
                            <th>Sad</th>
                        </tr>
                        <tr>
                            <td>{resultNLP.predictedDistribution.Calm}</td>
                            <td>{resultNLP.predictedDistribution.Happy}</td>
                            <td>{resultNLP.predictedDistribution.Sad}</td>

                        </tr>
                    </table>

                </Grid>
            </Grid>
            <br/>
            <br/>
        </>
    );
}

export default Hybrid;