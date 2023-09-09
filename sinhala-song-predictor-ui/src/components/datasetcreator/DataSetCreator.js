import React, {useEffect} from "react";
import Box from "@mui/material/Box";
import Loader from "../common/Loader";
import {Button, Checkbox, FormControlLabel, FormGroup, Grid} from "@mui/material";
import SongUploader from "../common/SongUploader";
import DataSetCreatorService from "../../service/DataSetCreatorService";
import DataSetCreatorGrid from "./DataSetCreatorGrid";

const DataSetCreator = () => {
    const [loading, setLoading] = React.useState(false);
    const [selectedFile, setSelectedFile] = React.useState();
    const [algorithmsSelected, setAlgorithmsSelected] = React.useState([]);
    const [activeData, setActiveData] = React.useState([]);
    const [totCount, setTotCount] = React.useState(0);
    const [pageSize, setPageSize] = React.useState(5);
    const [page, setPage] = React.useState(0);

    useEffect(() => {
        loadRowCount();
        loadSavedDate(page, pageSize);
    }, []);

    function loadRowCount() {
        DataSetCreatorService.getTotalRowCount()
            .then((response) => {
                if (response.success) {
                    setTotCount(response.data);
                } else {
                    alert("Error loadRowCount");
                }
            })
            .catch((ex) => {
                alert("Error loadRowCount");
            });
    }

    function loadSavedDate(page, pageSize) {
        setLoading(true);
        DataSetCreatorService.getAllPaginatedSavedData(page, pageSize)
            .then((response) => {
                if (response.success) {
                    setActiveData(response.data);
                    setLoading(false);
                } else {
                    alert("Error");
                    setLoading(false);
                }
            })
            .catch((ex) => {
                alert("Error");
                setLoading(false);
            });
    }


    const handleChange = (event) => {
        setAlgorithmsSelected({
            ...algorithmsSelected,
            [event.target.name]: event.target.checked,
        });
    };

    function extractSaveFeatures() {
        setLoading(true);
        DataSetCreatorService.saveAndExtractFeatures(algorithmsSelected, selectedFile)
            .then((response) => {
                if (response.success) {
                    console.log(response.data);
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

    const handlePageChange = (paginatedModel) => {
        console.log(paginatedModel.page);
        loadSavedDate(paginatedModel.page, paginatedModel.pageSize);
        setPage(paginatedModel.page);

    };
    return (
        <>
            <Box>
                <Box id="dataset-creator" className="dataset-creator">
                    {loading ? <Loader/> : null}
                    <h1> DataSet Creator</h1>
                    <h6>The inputs audio files would be saved and used for training deep learning algorithm for sinhala
                        music
                        classfication</h6>
                    <Grid id={'song-uploader'} container justifyContent="center">
                        <Grid item xs={12} sm={6} md={3}>
                            <SongUploader
                                setSelectedFile={setSelectedFile}
                            />
                        </Grid>
                    </Grid>
                    <Grid>
                        <FormGroup>
                            <FormControlLabel control={<Checkbox name={"SMO"} onClick={(e) => handleChange(e)}/>}
                                              label="SMO"/>
                            <FormControlLabel
                                control={<Checkbox name={"Naive-Bayes"} onClick={(e) => handleChange(e)}/>}
                                label="NAIVE BAYES"/>
                            <FormControlLabel
                                control={<Checkbox name={"Random-Forest"} onClick={(e) => handleChange(e)}/>}
                                label="RANDOM FOREST"/>

                            <FormControlLabel control={<Checkbox name={"K-Means"} onClick={(e) => handleChange(e)}/>}
                                              label="K MEANS"/>
                            <FormControlLabel
                                control={<Checkbox name={"Hierarchical-Clustering"} onClick={(e) => handleChange(e)}/>}
                                label="HIERARCHICAL CLUSTERING"/>
                        </FormGroup>
                    </Grid>
                    <Grid container justifyContent="flex-end">
                        <Button id="dataset-creator-button" size="medium" variant="contained" color='secondary'
                                style={{marginTop: "20px"}} onClick={() => extractSaveFeatures()}>EXTRACT PREDICT
                            FEATURES</Button>
                    </Grid>
                    <br/>

                </Box>
                <Grid>
                    {activeData.length > 0 ?
                        <DataSetCreatorGrid
                            activeData={activeData}
                            totCount={totCount}
                            pageSize={pageSize}
                            setPageSize={setPageSize}
                            handlePageChange={handlePageChange}
                            page={page}/> : null}
                </Grid>
            </Box>
        </>
    )
}

export default DataSetCreator;
